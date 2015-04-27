import sqlite3 as sq
from datetime import datetime,timedelta
import os

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

# Authentication related APIs
def get_salt(name):
	'''
		Return encrypted salt for a particular user to the admin interface to mix with
		encrypted password
	'''
	con=sq.connect('fms.db')
	cur=con.cursor()
	cur.execute('select salt from auth where user_name=(?)',(name,))
	x=cur.fetchone()
	if x==None:
		return("NULL")
	return x
	
def verify(name,passwd):
	'''
		Verify salt-added password as stored on the server
	'''
	con=sq.connect('fms.db')
	cur=con.cursor()
	cur.execute('select password from auth where user_name=(?)',(name,))
	key=cur.fetchone()
	if key[0]==passwd:
		return "true"
	else:
		return "false"
		
def post_participant_location(usn,lat,lon):
	'''
		Real Time tracking functionality of subscribers
	'''
	con=sq.connect('fms.db')
	cur=con.cursor()
	try:
		cur.execute('delete from realtime_user where usn=(?)',(usn,))
	except:
		pass
	try:
		cur.execute('insert into realtime_user values (?,?,?)',(usn,lat,lon))
		x = "true"
	except:
		x = "false"
	con.commit()
	return x

def post_vehicle_location(vehicle_id,lat,lon):
	'''
		Real time vehicle tracking
	'''
	con=sq.connect('fms.db')
	cur=con.cursor()
	try:
		cur.execute('delete from realtime_vehicle where vehicle_id=(?)',(vehicle_id,))
	except:
		pass
	try:
		cur.execute('insert into realtime_vehicle values(?,?,?)',(vehicle_id,lat,lon))
		x = "true"
	except:
		x = "false"
	con.commit()
	return x
	
def register_parent(usn,gcm_key):
	'''
		Adding GCM key to database when parent app is installed to enable GCM messaging to it
	'''
	con=sq.connect('fms.db')
	cur=con.cursor()
	try:
		cur.execute('insert into sos_notif values (?,?)',(usn,gcm_key))
		x = "true"
	except:
		x = "false"
	con.commit()
	return x
	
def notify_parent(vehicle_id):
	'''
		Notify the parent of transport reaching destination
	'''
	con = sq.connect("fms.db")
	cur = con.cursor()
	cur.execute('select usn from assigned_vehicles where vehicle_id=(?)',(vehicle_id,))
	l = cur.fetchall()
	for item in l:
		cur.execute('select gcm_key from sos_notif where usn=(?)',(item[0],))
		regid = cur.fetchone()[0]
		gcm = GCM("<project API key here>")
		data='Transport assigned to subscriber bearing USN: '+item[0]+' has safely reached the destination'
		gcm.plaintext_request(registration_id=regid, data=data)

def sos(usn,lat,lon):
	'''
		Participant raising a distress signal
	'''
	con = sq.connect("fms.db")
	cur = con.cursor()
	#cur.execute('select gcm_key from sos_notif where usn=(?)',(item[0],))
	#regid = cur.fetchone()[0]
	#gcm = GCM("<project API key here>")
	#global data
	#data='The subscriber bearing USN: '+item[0]+' has raised an emergency signal.\n Last reported location is: \n \
	#http://maps.google.com/?q='+lat+','+lon
	#gcm.plaintext_request(registration_id=regid, data=data)
	data = "Hello" #Fetch from User details table here
	f = open("/var/www/FlaskApp1/FlaskApp1/static/data.txt","w")
	f.write(data)
	f.close()

def admin_sos():
	'''
		Administrator endpoint receiving distress signal
	'''
	#global flag
	while(not(os.path.isfile("/var/www/FlaskApp1/FlaskApp1/static/data.txt"))):
		pass
	f = open("/var/www/FlaskApp1/FlaskApp1/static/data.txt")
	s = f.read();
	f.close()
	os.remove("/var/www/FlaskApp1/FlaskApp1/static/data.txt")
	return s
