from flask import Flask,json,render_template,url_for
import utils

app = Flask(__name__)


# Test route at the root path i.e. <ip>/api2/
@app.route('/')
def hello():
	#f = open("/var/www/FlaskApp1/FlaskApp1/static/test","w")
	#f.write("Testing\n")
	#f.close()
	return "Hello, World (2)"

# Endpoint for admin interface to receive any SOS notifications raised
@app.route('/api/admin/deferred')
def a_sos():
	print "Requested"
	b = utils.admin_sos()
	#print b
	return b

# starting the app	
if __name__ == '__main__':
	app.run(host='0.0.0.0', port=5001)
