from flask import Flask,json,render_template,url_for,request
import os
import sqlite3 as sq
import json
import utils
import requests
import read_data_new as rdf

app = Flask(__name__)

# Authorization related APIs
@app.route('/')
def hello():
	return "Hello,World"

@app.route('/api/authorize/<name>')
def salt(name):
	x=utils.get_salt(name)
	return x[0]
	
@app.route('/api/authorize/<name>/<passwd>')
def authorize(name,passwd):
	b=utils.verify(name,passwd)
	return b

# Location Posting APIs (Realtime tracking)
@app.route('/api/participant/live/<usn>/<lat>/<lon>')
def p_location(usn,lat,lon):
	b = utils.post_participant_location(usn,lat,lon)
	return b

@app.route('/api/vehicle/<vehicle_id>/<lat>/<lon>')
def p_vehicle_location(vehicle_id,lat,lon):
	b = utils.post_vehicle_location(vehicle_id,lat,lon)
	return b

# Feature related APIs (Notifications and SOS)
@app.route('/api/register/<usn>/<gcm_key>')
def register(usn,gcm_key):
	b = utils.register_parent(usn,gcm_key)
	return b

@app.route('/api/vehicle/arrival/<vehicle_id>/<status>')
def vehicle_notif(vehicle_id,status):
	b = utils.notify_parent(vehicle_id,status)
	return b

@app.route('/api/participant/sos/<usn>/<lat>/<lon>')
def p_sos(usn,lat,lon):
	b = utils.sos(usn,lat,lon)
	return b

# Visualization data
@app.route('/api/runalgo')
def send_vizualise():
#	os.system('chmod 777 /var/www/FlaskApp1/FLaskApp1/new.xlsx')
#	try:
#		sheets = rdf.csv_from_excel("/var/www/FlaskApp1/FlaskApp1/new.xlsx")
#		data = rdf.geocode_new(sheets)
#		rdf.cleanup(sheets)
#	except Exception as e:
#		data = str(e)
#	return json.dumps(data)
	con = sq.connect("/var/www/FlaskApp1/FlaskApp1/fms.db")
	cur = con.cursor()
	try:
		cur.execute('select * from user_geocodes')
		l = cur.fetchall()
	except Exception:
		con.commit()
		return "false"
	con.commit()
	return json.dumps(l)
	

@app.route('/api/fetchlist')
def send_studs():
	data = utils.sendstuds()
	return json.dumps(data)

@app.route('/api/allocated',methods=["POST"])
def add_allocs():
	data = request.form['data']
	b = utils.update_allocs(data)
	return b

@app.route('/api/fetchallocs')
def sendaloc():
	data = utils.stud_allocs()
	return json.dumps(data)

@app.route('/api/live/vehicle/<vehicle_id>')
def livetracking(vehicle_id):
	data = utils.live_vehicle(vehicle_id)
	return json.dumps(data)

# starting the app	
if __name__ == '__main__':
	app.run(host='0.0.0.0',port=5000)
