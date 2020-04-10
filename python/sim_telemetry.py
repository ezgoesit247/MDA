import sys
import codecs
import csv
import datetime
import numpy as np
import getopt
import pandas as pd
import time
import logging
import re
import os
from subprocess import check_output
import json

#ACCUMULATOR INDICES AND A IS ACCUMULATOR INITIAL VALUES
#acc_vars_num = [23,24,25,26,27,28,29,30,31,36,37]
acc_vars_num = ['metadata/2016','metadata/2017','metadata/2018','metadata/2019',
                'metadata/2020','metadata/2021','metadata/2022','metadata/2023',
                'metadata/2024','metadata/2029','metadata/2030']
                #2017,2018,2019,2020,2021,2022,2023,2024,2029,2030]
a =  [2.0,4.0,6.0,8.0,1.0,3.0,5.0,7.0,9.0,8.0,10.0]
out_header = ['metadata/1000','metadata/1001','metadata/1002',
              'metadata/1003','metadata/1004','metadata/1005','metadata/2000',
              'metadata/2001','metadata/2002','metadata/2003','metadata/2004',
              'metadata/2005','metadata/2006','metadata/2007','metadata/2008',
              'metadata/2009','metadata/2010','metadata/2011','metadata/2012',
              'metadata/2013','metadata/2014','metadata/2015','metadata/2016',
              'metadata/2017','metadata/2018','metadata/2019','metadata/2020',
              'metadata/2021','metadata/2022','metadata/2023','metadata/2024',
              'metadata/2025','metadata/2026','metadata/2027','metadata/2028',
              'metadata/2029','metadata/2030','metadata/2031','metadata/2032',
              'metadata/2033','metadata/2034','metadata/2035','metadata/2036',
              'metadata/2037','metadata/2038']

def read_work_order():
    input_work_order = pd.read_csv("work_order.csv")
    return input_work_order

def read_activity_table():
    input_activity = pd.read_csv('activity_type.csv')
    return input_activity 

        
def read_event_table():
    input_event = pd.read_csv('event_type.csv')
    return input_event 

    
def activity_duration():
    duration = read_activity_table().iloc[:,2]
    return duration

def activity_line_speed():
    line_speed = read_activity_table().iloc[:,3]
    return line_speed
            

def read_input(inputfile):                    
   #HARD CODED INPUT AND OUTPUT FILE NAMES WHEN RUNNING INSIDE SPYDER
    #inputfile = "dummy_data.csv"
    #encoding = 'utf-8'        
    #outputfile = "test_out5.csv"
    
#INITIALIZING MATRICES V = M * K + B. ---(M*K) = A
    data = []
    m = []
    input_header = []
    
#READS DATA FROM INPUT FILE, CONVERTS EVERYTHING TO FLOAT EXCEPT DATETIME TIMESTAMP
    with open(inputfile, "r") as fp:
        reader = csv.reader(fp)
        input_header = next(reader)
        for row in reader:
                data = [float(i) for i in row]
                m.append(data)
    
    return m

def timestamp():
    date = str(datetime.datetime.now())
    return date

def line_start(m, line_speed_val):
    #SET LINE SPEED TO SOME VALUE SPECIFIED IN SIM_RUN FUNCTION
    m[0][4] = line_speed_val
    return m

def matrix_ops(m):                    
#READS K (VARIABLE) AND B (BIAS) MATRICES AND CONVERTS TO FLOATS
    data_k = []
    matrix_k = []
    a = []
    b = []
    in_file_k = "matrix_k.csv"
                
    with codecs.open(in_file_k, "r","utf-8") as fk:
        reader_k = csv.reader(fk)
        for row_k in reader_k:
            data_k = [float(ik) for ik in row_k]
            matrix_k.append(data_k)
    in_file_b = "matrix_b.csv"            
                
    with codecs.open(in_file_b, "r", "utf-8") as fb:
        reader_b = csv.reader(fb)
        for row_b in reader_b:
            b = [float(ib) for ib in row_b]
            
#SPLITS EACH ROW OF M INTO TIMESTAMP PORTION AND VARIABLE DATA PORTION       
    m_data = []            
    #for x in range(len(m)):
    m_data.append(m)
    #m_reelndx = []
    #for xreel in range(len(m)):
    #m_reelndx.append(m[-1])
    
#MATRIX MULTIPLICATION OF M*K AND ADDITION OF B TO GET MATRIX V
    a =  [[sum(x*y for x,y in zip(m_row,matrix_k_col)) for matrix_k_col in zip(*matrix_k)]
            for m_row in m_data]    
    
    v = []
    v = [[a[i2][j2] + b[j2] for j2 in range(len(a[0]))] for i2 in range(len(a))]
    
#OUTPUT FILE VARIABLE NAMES
    '''out_header = ['Timestamp','in_oven_heater_tmp', 'in_capstan_tension', 'in_taper1_rpm',
                  'in_taper2_', 'in_line_speed','in_taper2_rpm',
                  'oven_heater_tmp','capstan_tension', 'taper1_rpm', 'taper2_',
                  'line_speed','taper2_rpm','pred_impedance', 'pred_insertion_loss',
                  'oven_voltage','oven_current','oven_watts','oven_heater','oven_load',
                  'oven_inlet_tmp','oven_exit_tmp','chiller_exit_tmp','chiller_mix',
                  'chiller_inlet_tmp','chiller_wash_tmp','oven_load2','room_humidity',
                  'room_tmp','payoff_run_length','taper1_inlet_tension',
                  'taper1_dispenser_diameter','taper1_reel_capacity','taper1_dispenser',
                  'taper2_reel_capacity','taper1_outlet_tension','taper2_inlet_tension',
                  'taper2_dispenser1','taper2_dispenser2','capstan_air',
                  'payoff1_tension','payoff2_tension','drain_tension',
                  'taper1_dispenser_tension','taper2_dispenser_tension','takeup_tension',
                  'ReelNdx']''' 
#ACCUMULATOR VARIABLES AND THEIR INDICES. TASK 122 IN VSTS
    '''acc_vars = ['taper1_inlet_tension','taper1_dispenser_diameter',
              'taper1_reel_capacity','taper1_dispenser','taper2_reel_capacity',
              'taper1_outlet_tension','taper2_inlet_tension','taper2_dispenser1',
              'taper2_dispenser2','taper1_dispenser_tension','taper2_dispenser_tension']'''
    #acc_vars_num = [23,24,25,26,27,28,29,30,31,36,37]
    #a =  [2,4,6,8,1,3,5,7,9,8,10]


#ADDS ACCUMULATOR VALUE 
#add outer for loop for 'after certain count' part
    '''count = 200
    for cnt in range(len(count)):'''
    for i4 in range(len(v)):
        for j4 in range(len(v[i4])):
            for acc_index in range(len(acc_vars_num)):
                if j4 == acc_vars_num[acc_index]:
                    a[0][acc_index] += v[i4][j4]
                    v[i4][j4] = a[0][acc_index]
                    #v[i4][j4] += init //v = v + init 
                else:
                    pass

#COMBINES TIMESTAMP, INPUT AND OUTPUT VALUES INTO ONE ROW                
    for i3 in range(len(m_data)):
        m_data[i3].extend(v[i3])
        #m_data[i3].insert(0,m_ts[i3])
        #insert accumulator values in specified indices
    return m_data

def melt(m_data, outputfile):
    #MELTS DATA TO CSV FORMAT THAT IS REQUIRED
    #outputfile = "test_out6.csv"
    df = pd.DataFrame(data = m_data, columns = out_header)
    melted = pd.melt(df, id_vars = ['Timestamp','ReelNdx'])
    melted.to_csv(outputfile)
    
def line_stop(m, line_speed_val, tension_val):
    #SET LINE SPEED AND TENSION TO 0
    m[0][1] = tension_val
    m[0][4] = line_speed_val
    return m      

def reel_change(m_data):
    #reset to initial accumulator values
    for i in range(len(m_data[0])):
        for j in range(len(acc_vars_num)):
            if i == acc_vars_num[j]:
                m_data[0][i] = a[j]
    return m_data

current_milli_time = lambda: int(round(time.time() * 1000))

format_date_for_sql = lambda: str(datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")).replace(' ','T')

usage = lambda: print("usage: "+os.path.basename(__file__)+' -i <inputfile>', "\n\tno input file")

## METHOD SIGNATURE IS AS SUCH: get_post_telemetry_command(STRING, STRING, ARRAY[[STRING, FLOAT_AS_STRING],[STRING, FLOAT_AS_STRING]],... etc, STRING)
def get_post_telemetry_command(server, uri, variables, reeluri, timestamp):
  s =   build_telemetry_front(reeluri, timestamp) +  build_telemetry_middle(variables) + build_telemetry_back(server, uri)
  return s

def build_telemetry_front(reeluri, timestamp):
    s = 'echo {"reel": "'+reeluri+'","timestamp": "'+timestamp+'"';
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

def create_extruder_reel(workorderuri,reelidentifier="A_UNIQUE_WORKORDER"+str(current_milli_time())):
    return create_reel(workorderuri,reelidentifier, "EXTRUDER")

def create_taper_reel(workorderuri,reelidentifier="A_UNIQUE_WORKORDER"+str(current_milli_time())):
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



## SOME GLOBAL VARIABLES FOR GOOD MEASURE
SERVICE_URL = 'http://localhost:8080'
POST_TO_EVENTHUB_URI = '/evthub'
POST_TO_TELEMETRY_URI = '/telemetry'
POST_TO_REEL_URI = '/reel'
POST_TO_WORKORDER_URI = '/workorder'

def main(argv):
    log = os.path.basename(__file__) + ".log"
    logging.basicConfig(format='[%(asctime)s][%(levelname)s] %(message)s',filename=log,level=logging.DEBUG)

    inputfile = ''
    #inputfile = 'dummy_data.csv'
    outputfile = ''
    #outputfile = 'test_output.csv'
    encoding = 'utf-8'
    try:
        opts, args = getopt.getopt(argv, "hi:o:",["ifile=","ofile="])
    except getopt.GetoptError:
        print('myfile.py -i <inputfile> -o <outputfile>')
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print('myfile.py -i <inputfile> -o <outputfile>')
            sys.exit()
        elif opt in ("-i" , "--ifile"):
            inputfile = arg
        elif opt in ("-o", "--ofile"):
            outputfile = arg
        print('Input file is "', inputfile)
        print('Output file is "', outputfile)
    #SIMULATION RUN
    m_input = read_input(inputfile)
    #line_start(m_input,activity_line_speed().iloc[0])
    t_sim = time.time() + activity_duration().iloc[0]*60

    workorderuri = create_workorder()
    reeluri = create_taper_reel(workorderuri)
    
    while time.time() < t_sim:
        for i in range(len(m_input)):
            if time.time() < t_sim:
                m_input_row = m_input[i]
                x = matrix_ops(m_input_row)
                variables = [list(c) for c in zip(out_header, x[0])]
                for list_row in range(len(variables)):
                    for list_col in range(len(variables[list_row])):
                        variables[list_row][1] = str(variables[list_row][1])
                print(variables)
                #melt(x,outputfile)
                cmd=get_post_telemetry_command(SERVICE_URL,POST_TO_TELEMETRY_URI,variables,reeluri,format_date_for_sql())
                logging.info("Command to post:\n"+cmd)
                ouptut_bytes = check_output(cmd, shell=True)
                json_string = ouptut_bytes.decode()
                logging.info(json_string)
                print("telemetry added:"+json_string)
                time.sleep(3)

            else:
                pass
        
#Line stop and reel change event
    '''read_input(inputfile)
    line_stop(read_input(inputfile), 0.0, activity_line_speed().iloc[1])
    t_stop = time.time() + activity_duration().iloc[1]
    while time.time() < t_stop:
        for j in range(len(m_input)):
            if time.time() < t_stop:
                m_input_change_row = m_input[j]
                y = reel_change(matrix_ops(m_input_row))
                #INPUT VAR_VAL FUNCTION
                print(y)
                melt(y,outputfile)
                time.sleep(3)'''
        
if __name__ == "__main__":
    main(sys.argv[1:])
