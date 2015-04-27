import xlrd
import re
import csv
import geocoder
import os
import sqlite3 as sq
#----------------------------------------------------------------------
def update_userinfo_db(l):
    con = sq.connect("/var/www/FlaskApp1/FlaskApp1/fms.db")
    con.text_factory=str
    cur = con.cursor()
    new = []
    for i in l:
        if(i[0]=="userid"):
            continue
        new.append(tuple(i))
    #print new
    cur.execute("delete from user_details");
    cur.executemany("insert into user_details values(?,?,?)",new)
    con.commit()

def csv_from_excel(excel_file):
    workbook = xlrd.open_workbook(excel_file)
    all_worksheets = workbook.sheet_names()
    for worksheet_name in all_worksheets:
        worksheet = workbook.sheet_by_name(worksheet_name)
        your_csv_file = open(''.join(['/var/www/FlaskApp1/FlaskApp1/',worksheet_name,'.csv']), 'wb')
        wr = csv.writer(your_csv_file, quoting=csv.QUOTE_ALL)
        for rownum in xrange(worksheet.nrows):
            wr.writerow([unicode(entry).encode("utf-8") for entry in worksheet.row_values(rownum)])
        your_csv_file.close()
	return all_worksheets

def geocode_new(sheets):
    """ Geocode all string addresses into lat,long format
        and writes into geolocations_people file"""
    blr_add = []
    all_add=[]
    p1 = re.compile(r"bangalore|blore|b\'lore|b\ lore",re.I)
    for sheet in sheets:
        if(os.path.isfile('/var/www/FlaskApp1/FlaskApp1/'+str(sheet)+'.csv')):
            reader=csv.reader(open('/var/www/FlaskApp1/FlaskApp1/'+str(sheet)+'.csv','rb'))
            for i in reader:
                if(re.search(p1,i[2])):
                    blr_add.append(list((i[0],i[2])))
                    all_add.append(list(i))
    update_userinfo_db(all_add)
    '''print len(blr_add)
    usns_only = set()
    for i in blr_add:
        if i[0] in usns_only:
            print i[0]
        else:
            usns_only.add(i[0])
    print len(usns_only)	
    '''
    clean_add=[]
    for i in blr_add:
        if(re.search(r'\n',i[1])):
            m = i[1].split("\n")
            g = geocoder.google(m[1].strip())
        else:
            g = geocoder.google(i[1].strip())
        if(g.latlng):
            if(g.latlng[0]<0 or g.latlng[1]<0):
                continue
            else:
                print(i[0],g.latlng)
                clean_add.append((i[0].strip(),g.latlng[0],g.latlng[1]))
    
    print len(clean_add)

#    with open("geolocations_people_test","w") as f:
#        for i in clean_add:
#            f.write(str(i[0])+","+str(i[1])+","+str(i[2])+"\n")
    return clean_add

def cleanup(sheets):
    for i in sheets:
        if(os.path.isfile('/var/www/FlaskApp1/FlaskApp1/'+str(i)+'.csv')):
            os.remove('/var/www/FlaskApp1/FlaskApp1/'+str(i)+'.csv')
#----------------------------------------------------------------------
if __name__ == "__main__":
    #path = "new.xlsx"
    sheets = csv_from_excel('/var/www/FlaskApp1/FlaskApp1/new.xlsx')
    geocode_new(sheets)
    cleanup(sheets)
#----------------------------------------------------------------------

