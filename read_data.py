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
    for i in range(book.nsheets):
        sheet = book.sheet_by_index(i)
        for j in range(sheet.nrows):
            cell = sheet.cell(j,0)
            l.append(cell.value)
    return l


def geocode(l):
    """ Geocode all string addresses into lat,long format
        and writes into geolocations_people file"""
    blr_add = []
    p1 = re.compile("bangalore",re.I)
    for i in l:
        if(re.search(p1,i)):
            blr_add.append(i)

    clean_add=[]
    p2 = re.compile("1PI1\dCSDIP\d\d\d |1PI1\dCS\d\d\d",re.I)

    for i in blr_add:
        m = re.split(p2,i)
        if(len(m)>=2):
            g = geocoder.google(m[1].strip())
            if(g.latlng):
                print(g.latlng)
                clean_add.append(g.latlng)

    with open("geolocations_people","w") as f:
        for i in clean_add:
            f.write(str(i[0])+","+str(i[1])+"\n")
 
#----------------------------------------------------------------------
if __name__ == "__main__":
    path = "data.xls"
    address_list = get_data(path)
    geocode(address_list)
#----------------------------------------------------------------------

