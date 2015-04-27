import sqlite3 as sq
from datetime import datetime,timedelta
import os
import json
from gcm import *

'''
	Module containing all functions that are supported by the Fleet Management Ecosystem
	Contains functions to:
	1) Accept an excel sheet and save user information
	2) Geocode all subscribers' addresses
	3) Return these results from step 2 to a javascript clustering algorithm
	4) Accept results from the above with vehicles allocated to each subscriber and persist the results
	5) Allow real time tracking of subscriber
	6) Allow real time tracking of vehicle
	7) Register parent app to receive SOS notifications
	8) Provide details to admin interface regarding current position and student details
	9) Trigger Google Cloud Messaging, server to client communication for notiication on vehicle reaching or leaving dest
	10) SOS notifications from subscribers' app
'''

# Authentication related API functionality
def get_salt(name):
	'''
		Returns salt for a given username to the admin interface for login
	'''
	con=sq.connect('/var/www/FlaskApp1/FlaskApp1/fms.db')
	cur=con.cursor()
	cur.execute('select salt from auth where user_name=(?)',(name,))
	x=cur.fetchone()
	if x==None:
		return("NULL")
	return x
	
def verify(name,passwd):
	'''
		Verify password+salt combination on the server side
	'''
	con=sq.connect('/var/www/FlaskApp1/FlaskApp1/fms.db')
	cur=con.cursor()
	cur.execute('select password from auth where user_name=(?)',(name,))
	key=cur.fetchone()
	if key[0]==passwd:
		return "true"
	else:
		return "false"
		
# Real time Tracking API functionality
def post_participant_location(usn,lat,lon):
	'''
		Log participant's latest location from the App
	'''
	con=sq.connect('/var/www/FlaskApp1/FlaskApp1/fms.db')
	cur=con.cursor()
	try:
		cur.execute('delete from realtime_user where usn=(?)',(usn,))
	except Exception:
		pass
	try:
		cur.execute('insert into realtime_user values (?,?,?)',(usn,lat,lon))
		x = "true"
	except Exception:
		x = "false"
	con.commit()
	return x

def post_vehicle_location(vehicle_id,lat,lon):
	'''
		Log vehicle's latest location from the App
	'''
	con=sq.connect('/var/www/FlaskApp1/FlaskApp1/fms.db')
	cur=con.cursor()
	try:
		cur.execute('delete from realtime_vehicle where vehicle_id=(?)',(vehicle_id,))
	except Exception:
		pass
	try:
		cur.execute('insert into realtime_vehicle values(?,?,?)',(vehicle_id,lat,lon))
		x = "true"
	except Exception:
		x = "false"
	con.commit()
	return x
	
# Notification related API functionality
def register_parent(usn,gcm_key):
	'''
		Register the parent version of the app to receive notifications relating to SOS and vehicle arrival at destination.
	'''
	con=sq.connect('/var/www/FlaskApp1/FlaskApp1/fms.db')
	cur=con.cursor()
	try:
		cur.execute('insert into sos_notif values (?,?)',(usn,gcm_key))
		x = "true"
	except Exception:
		x = "false"
	con.commit()
	return x
	
def notify_parent(vehicle_id,status):
	'''
		Trigger the sending of a notification through Google Cloud Messaging in the event of vehicle reaching or leaving destination
	'''
	con = sq.connect('/var/www/FlaskApp1/FlaskApp1/fms.db')
	cur = con.cursor()
	try:
		cur.execute('select usn,gcm_key from assigned_vehicles natural join sos_notif where vehicle_id=(?)',(vehicle_id,))
		l = cur.fetchall()
		for item in l:
			#cur.execute('select gcm_key from sos_notif where usn=(?)',(item[0],))
			#regid = cur.fetchone()[0]
			gcm = GCM("AIzaSyA_dwgg9ubyOxOb8SR8V9-kmdkuFKQvCRc")
			if int(status)==1:
				data={"info":'Transport assigned to subscriber bearing USN: '+item[0]+' has safely reached the destination'}
			else:
				data ={"info":'Transport assigned to subscriber bearing USN: '+item[0]+' has begun return journey'}
			gcm.plaintext_request(registration_id=item[1], data=data)
	except Exception:
		return "false"
	return "true"

# SOS
def sos(usn,lat,lon):
	'''
		Capture distress signal raised by participant and notify parent and admin interface
	'''
	con = sq.connect('/var/www/FlaskApp1/FlaskApp1/fms.db')
	cur = con.cursor()
	try:
		cur.execute('select gcm_key from sos_notif where usn=(?)',(usn,))
		regid = cur.fetchone()[0]
	except Exception:
		return "false"
	gcm = GCM("AIzaSyA_dwgg9ubyOxOb8SR8V9-kmdkuFKQvCRc")
	#global data
	data={"usn":str(usn),"lat":str(lat),"lon":str(lon)}
	gcm.plaintext_request(registration_id=regid, data=data)
	#Fetch from User details table here
	f = open("/var/www/FlaskApp1/FlaskApp1/static/data.txt","w")
	f.write(str(data))
	f.close()
	return "true"

def admin_sos():
	'''
		Administrator interface receiving the distress signal
	'''
	while(not(os.path.isfile("/var/www/FlaskApp1/FlaskApp1/static/data.txt"))):
		pass
	f = open("/var/www/FlaskApp1/FlaskApp1/static/data.txt")
	s = f.read();
	f.close()
	os.remove("/var/www/FlaskApp1/FlaskApp1/static/data.txt")
	return s

# Miscellaneous gluing functionality
def sendstuds():
	'''
		Send back details of which student is allotted which vehicle along with personal details to the administrator interface
	'''
	con=sq.connect('/var/www/FlaskApp1/FlaskApp1/fms.db')
        cur=con.cursor()
	try:
		cur.execute('select user_details.usn,name,address,vehicle_id from user_details natural join assigned_vehicles')
        	l = cur.fetchall()
	except Exception:
		l = "false"
	con.commit()
	return l

def update_allocs(data):
	'''
		Allow the JS markerclusterer to annotate subscribers with transport based on FMS algortihm
	'''
	con=sq.connect('/var/www/FlaskApp1/FlaskApp1/fms.db')
        cur=con.cursor()
	l = json.loads(data)
	try:
                cur.execute('delete from assigned_vehicles')
        except Exception as e:
                print e
        try:
		for i in l:
                	cur.execute('insert into assigned_vehicles values(?,?,?,?)',tuple(i))
                x = "true"
        except Exception as e:
                x = "false"
		print e
        con.commit()
        return x

def stud_allocs():
	'''
		Returns the annotated subscribers' location details and vehicle allocation information to admin interface and cost calculator
	'''
	con = sq.connect('/var/www/FlaskApp1/FlaskApp1/fms.db')
	cur = con.cursor()
	try:
		cur.execute('select * from assigned_vehicles')
		l = cur.fetchall()
	except Exception:
		l="false"
	con.commit()
	return l

def live_vehicle(vehicle):
	'''
		Returns the latest updated vehicle location for the admin interface to visualise
	'''
	con = sq.connect('/var/www/FlaskApp1/FlaskApp1/fms.db')
	cur = con.cursor()
	try:
		cur.execute('select * from realtime_vehicle where vehicle_id=(?)',(vehicle,))
		i = cur.fetchone()
	except Exception:
		i ="false"
	con.commit()
	return i
