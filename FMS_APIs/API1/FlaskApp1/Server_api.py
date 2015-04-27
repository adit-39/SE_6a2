from flask import Flask,json,render_template,url_for
import utils
import requests

app = Flask(__name__)

# Authorization related APIs
@app.route('/api/authorize/<name>')
def salt(name):
	x=utils.get_salt(name)
	return json.dumps(x[0])
	
@app.route('/api/authorize/<name>/<passwd>')
def authorize(name,passwd):
	b=utils.verify(name,passwd)
	return json.dumps(b)

# Location Posting APIs (Realtime tracking)
@app.route('/api/participant/<usn>/<lat>/<lon>')
def p_location(usn,lat,lon):
	b = utils.post_participant_location(usn,lat,lon)
	return json.dumps(b)

@app.route('/api/vehicle/<vehicle_id>/<lat>/<lon>')
def p_vehicle_location(vehicle_id,lat,lon):
	b = utils.post_vehicle_location(vehicle_id,lat,lon)
	return json.dumps(b)

# Feature related APIs (Notifications and SOS)
@app.route('/api/register/<usn>/<gcm_key>')
def register(usn,gcm_key):
	b = utils.register_parent(usn,gcm_key)
	return json.dumps(b)

@app.route('/api/vehicle/arrival/<vehicle_id>')
def vehicle_notif(vehicle_id):
	b = utils.notify_parent(vehicle_id)
	return json.dumps(b)

@app.route('/api/participant/sos/<usn>/<lat>/<lon>')
def p_sos(usn,lat,lon):
	b = utils.sos(usn,lat,lon)
	return json.dumps(b)

# Visualization data
@app.route('/api/admin/visualize/data')
def send_vizualise():
	return

# starting the app	
if __name__ == '__main__':
	app.run(host='0.0.0.0',debug=True, port=5000)
