"""
	Database initialization script
"""
import sqlite3 as sq
con = sq.connect("fms.db")
cur = con.cursor()

cur.execute("create table if not exists auth(user_name text primary key, \
											password text not null, \
											salt text not null)")

cur.execute("create table if not exists assigned_vehicles(usn text primary key,\
											lat text not null, \
											lon text not null, \
											vehicle_id text not null)")

cur.execute("create table if not exists realtime_user(usn text primary key,\
										lat text not null,\
										lon text not null)")

cur.execute("create table if not exists realtime_vehicle(vehicle_id text primary key,\
														lat text not null, \
														lon text not null)")
									
cur.execute("create table if not exists sos_notif(usn text primary key,\
													gcm_key text not null)")

cur.execute("create table if not exists user_geocodes(usn text primary key, \
														lat text not null,	\
														lon text not null)")
con.commit()
