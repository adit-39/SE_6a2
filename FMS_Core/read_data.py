import xlrd
import re
import geocoder
#----------------------------------------------------------------------
def get_data(path):
    """
    Open and read an Excel file
    """
    book = xlrd.open_workbook(path)
    l=[]
    # print number of sheets
    names = ["4th Sem","6th Sem","8th Sem"]
    for i in names:
        sheet = book.sheet_by_name(i)
        for j in range(sheet.nrows):
            cell = sheet.cell(j,0)
            l.append(cell.value)
    #print(l)
    return l


def geocode(l):
    """ Geocode all string addresses into lat,long format
        and writes into geolocations_people file"""
    blr_add = []
    p1 = re.compile(r"bangalore|blore|b\'lore|b\ lore",re.I)
    for i in l:
        if(re.search(p1,i)):
            blr_add.append(i)

    clean_add=[]
    temp_usn=255
    p2 = re.compile("1PI1\dCSDIP\d\d\d |1PI1\dCS\d\d\d",re.I)

    for i in blr_add:
        usn_obj = re.search(p2,i)
        if(usn_obj):
            usn = usn_obj.group(0);
        else:
            usn = "1PI12CS"+str(temp_usn)
            temp_usn+=1
        m = re.split(p2,i)
        if(len(m)>=2):
            g = geocoder.google(m[1].strip())
            if(g.latlng):
                print(usn,g.latlng)
                clean_add.append((usn,g.latlng[0],g.latlng[1]))

    with open("geolocations_people_test","w") as f:
        for i in clean_add:
            f.write(str(i[0])+","+str(i[1])+","+str(i[2])+"\n")

#----------------------------------------------------------------------
if __name__ == "__main__":
    path = "data.xls"
    address_list = get_data(path)
    geocode(address_list)
#----------------------------------------------------------------------

