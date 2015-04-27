#!/usr/bin/env python
import read_data_new as rdf
import sqlite3 as sq
import os

def send_vizualise():
	#while(not(os.path.isfile("/var/www/FlaskApp1/FlaskApp1/new.xlsx"))):
	#	pass
	con = sq.connect("/var/www/FlaskApp1/FlaskApp1/fms.db")
	cur = con.cursor()
        os.system('chmod 777 /var/www/FlaskApp1/FlaskApp1/new.xlsx')
        try:
                sheets = rdf.csv_from_excel("/var/www/FlaskApp1/FlaskApp1/new.xlsx")
                for i in sheets:
			if(os.path.isfile('/var/www/FlaskApp1/FlaskApp1/'+str(i)+'.csv')):
				os.system('chmod 777 /var/www/FlaskApp1/FlaskApp1/'+str(i)+".csv")
		data = rdf.geocode_new(sheets)
                rdf.cleanup(sheets)
		x = True
        except Exception as e:
                data = str(e)
		print data
        	x = False

	if(x):
		print ("Hello")
		try:
			cur.execute("delete from user_geocodes")
		except Exception:
			pass
		try:
			for i in data:
				cur.execute("insert into user_geocodes values(?,?,?)",(i[0],i[1],i[2]))
		except Exception:
			print "Bad stuff"
			x = False
	con.commit()
	#os.remove("/var/www/FlaskApp1/FlaskApp1/new.xlsx")
	return x

if __name__=="__main__":
	x = send_vizualise()
	print x
