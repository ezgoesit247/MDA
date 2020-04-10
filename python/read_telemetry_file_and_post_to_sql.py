import logging
import datetime
import time
import re
import os
import sys, getopt
from subprocess import check_output
import json

current_milli_time = lambda: int(round(time.time() * 1000))

format_date_for_sql = lambda: str(datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")).replace(' ','T')

usage = lambda: print("usage: "+os.path.basename(__file__)+' -i <inputfile>', "\n\tno input file")

## METHOD SIGNATURE IS AS SUCH: get_post_telemetry_command(STRING, STRING, ARRAY[[STRING, FLOAT_AS_STRING],[STRING, FLOAT_AS_STRING]],... etc, STRING, STRING, STRING)
def get_post_telemetry_command(server, uri, variables, reeluri, timestamp, reelidentifier='', sendstatus='NOSEND'):
  s =   build_telemetry_front(reeluri, timestamp, reelidentifier, sendstatus) +  build_telemetry_middle(variables) + build_telemetry_back(server, uri)
  return s

def build_telemetry_front(reeluri, timestamp, reelidentifier, sendstatus):
    s = 'echo {"reel": "'+reeluri+'","timestamp": "'+timestamp+'","reelidentifier": "'+reelidentifier+'","sendstatus": "'+sendstatus+'"';
    return s

def build_telemetry_middle(variables):
    s = ', "variables":['
    for cnt, r in enumerate(variables):
        if(cnt > 0):
            s+=','
        s+='{ "variable":"'+r[0]+'","value":'+r[1]+' }'
    return s

def build_telemetry_back(server, uri):
    s = ']} | curl -X POST -H "Content-Type:application/json" -d @- '+ server + uri
    return s

def get_post_reel_command(server, uri, workorderuri, reelidentifier, type):
    s='echo {"workorder":"'+workorderuri+'","reelidentifier":"'+reelidentifier+'","type":"'+type+'"}'
    s+=' | curl -X POST -H "Content-Type:application/json" -d @- '+ server + uri
    return s

def get_post_workorder_command(server, uri, workorderidentifier, timestamp):
    s='echo {"workorderidentifier":"'+workorderidentifier+'","timestamp":"'+timestamp+'"}'
    s+=' | curl -X POST -H "Content-Type:application/json" -d @- '+ server + uri
    return s

def get_infile(argv):
    inputfile=''
    try:
        opts, args = getopt.getopt(argv,"hi:o:",["ifile="])
    except getopt.GetoptError:
        print (os.path.basename(__file__)+' -i <inputfile>')
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print (os.path.basename(__file__)+' -i <inputfile>')
            sys.exit()
        elif opt in ("-i", "--ifile"):
            inputfile = arg
    return inputfile

def send_telemetry_by_file_read(argv):
    filepath = get_infile(argv)
    if(filepath == ""):
        usage()
        sys.exit()
    if not(os.path.isfile(filepath)):
        usage()
        sys.exit()
    with open(filepath) as fp:  
        for cnt, line in enumerate(fp):
## ADD THE LETTER 'T' BETWEEN THE DATE PART AND THE TIME PART
            if(cnt == 0):
                continue
## SPLIT & STRIP THE LINE INTO AN ARRAY
            ary=[x.strip() for x in line.split(',')]
            reeluri=ary[0]
            reelidentifier=ary[1]
            sendstatus=ary[2]
            variables=[ [ ary[3],ary[4] ], [ ary[5],ary[6] ], [ ary[7],ary[8] ] ]
## BUILD CURL COMMAND
            cmd = get_post_telemetry_command(SERVICE_URL,POST_TO_TELEMETRY_URI,variables,reeluri,format_date_for_sql(),reelidentifier,sendstatus)
            logging.info("Executing: "+cmd)
            ouptut_bytes = check_output(cmd, shell=True)
            json_string = ouptut_bytes.decode()
            logging.info(json_string)
##  PAUSE FOR 5 SECONDS AND LOOP
            if(sendstatus == 'NEW'):
                time.sleep(5)

      
def create_workorder(workorderidentifier="A_UNIQUE_WORKORDER"+str(current_milli_time())):
## BUILD CURL COMMAND TO MAKE A WORKORDER
    cmd = get_post_workorder_command(SERVICE_URL,POST_TO_WORKORDER_URI,workorderidentifier,format_date_for_sql())
    ouptut_bytes = check_output(cmd, shell=True)
    json_string = ouptut_bytes.decode()
    logging.info(json_string)
## CONVERT TO JSON OBJECT
    workorder = json.loads(json_string)
    workorderuri = workorder['uri']
    print("workorder created:"+workorderuri)
    return workorderuri

def create_extruder_reel(workorderuri,reelidentifier="A_UNIQUE_REEL"+str(current_milli_time())):
    return create_reel(workorderuri,reelidentifier, "EXTRUDER")

def create_taper_reel(workorderuri,reelidentifier="A_UNIQUE_REEL"+str(current_milli_time())):
    return create_reel(workorderuri,reelidentifier, "TAPER")

def create_reel(workorderuri,reelidentifier, type):
## BUILD CURL COMMAND TO MAKE A REEL
    cmd = get_post_reel_command(SERVICE_URL,POST_TO_REEL_URI,workorderuri,reelidentifier,type)
    ouptut_bytes = check_output(cmd, shell=True)
    json_string = ouptut_bytes.decode()
    logging.info(json_string)
## CONVERT TO JSON OBJECT
    reel = json.loads(json_string)
    reeluri = reel['uri']
    print("reel created:"+reeluri)
    return reeluri

def doArg():
    print("Arg")

def doNoArg():
    print("No Arg")

## SOME GLOBAL VARIABLES FOR GOOD MEASURE
SERVICE_URL = 'http://localhost:8080'
POST_TO_EVENTHUB_URI = '/evthub'
POST_TO_TELEMETRY_URI = '/telemetry'
POST_TO_REEL_URI = '/reel'
POST_TO_WORKORDER_URI = '/workorder'

def main(argv):
    log = os.path.basename(__file__) + ".log"
    logging.basicConfig(format='[%(asctime)s][%(levelname)s] %(message)s',filename=log,level=logging.DEBUG)


    inputfile=''
    opts, args = getopt.getopt(argv,"hi:o:",["ifile="])
    for opt, arg in opts:
        if opt == '-h':
            print ("2"+os.path.basename(__file__)+' -i <inputfile>')
            sys.exit()
        elif opt in ("-i", "--ifile"):
            send_telemetry_by_file_read(argv)
            sys.exit()
    
## BUILD THE VARIABLES ARRAY
    variables=[["metadata/2000","11.11"],["metadata/2027","22.22"],["metadata/2038","33.33"]]
    cmd=get_post_telemetry_command(SERVICE_URL,POST_TO_TELEMETRY_URI,variables,create_taper_reel(create_workorder()),format_date_for_sql())
    ouptut_bytes = check_output(cmd, shell=True)
    json_string = ouptut_bytes.decode()
    logging.info(json_string)
    print("telemetry added:"+json_string)




if __name__== "__main__":
  main(sys.argv[1:])
