import sqlite3 as sq
from datetime import datetime,timedelta

count = 0

def get_salt(name):
	con=sq.connect('pesit_admins')
	cur=con.cursor()
	cur.execute('select salt from auth where user_name=(?)',(name,))
	x=cur.fetchone()
	if x==None:
		return("NULL")
	return x
	
def verify(name,passwd):
	con=sq.connect('pesit_admins')
	cur=con.cursor()
	cur.execute('select password from auth where user_name=(?)',(name,))
	key=cur.fetchone()
	if key[0]==passwd:
		return "TRUE"
	else:
		return "FALSE"
		
def get_time(activities):
	pass
