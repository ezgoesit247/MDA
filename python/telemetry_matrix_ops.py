import codecs
import csv
import datetime
import numpy as np
import sys
import getopt
import pandas as pd

#Make vector v reset after each iteration. So just reset v inside a loop 
#RUNS FILE FROM POWERSHELL WITH INPUT AND OUTPUT FILE NAMES
#TO RUN FROM POWERSHELL, OPEN IN CORRECT DIRECTORY, 
#py telemetry_matrix_ops.py -i dummy_data.csv -o test_out.csv
def main(argv):
    inputfile = ''
    outputfile = ''
    encoding = 'utf-8'
    try:
        opts, args = getopt.getopt(argv, "hi:o:",["ifile=","ofile="])
    except getopt.GetoptError:
        print(os.path.basename(__file__) + '  -i <inputfile> -o <outputfile>')
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
        print(os.path.basename(__file__) + '  -i <inputfile> -o <outputfile>')
            sys.exit()
        elif opt in ("-i" , "--ifile"):
            inputfile = arg
        elif opt in ("-o", "--ofile"):
            outputfile = arg
        print('Input file is "', inputfile)
        print('Output file is "', outputfile)
                

#HARD CODED INPUT AND OUTPUT FILE NAMES WHEN RUNNING INSIDE SPYDER
    inputfile = "dummy_data.csv"
    encoding = 'utf-8'        
    outputfile = "test_out5.csv"
    
#INITIALIZING MATRICES V = M * K + B. ---(M*K) = A
    data = []
    m = []
    data_k = []
    matrix_k = []
    a = []
    b = []
    input_header = []
    
#READS DATA FROM INPUT FILE, CONVERTS EVERYTHING TO FLOAT EXCEPT DATETIME TIMESTAMP
    with codecs.open(inputfile, "r", encoding) as fp:
        reader = csv.reader(fp)
        input_header = next(reader)
        for row in reader:
                data = [float(i) if i.isdigit() else
                        datetime.datetime.strptime(i,"%m/%d/%Y %H:%M:%S") for i in row]
                m.append(data)
                
#READS K (VARIABLE) AND B (BIAS) MATRICES AND CONVERTS TO FLOATS
    in_file_k = "matrix_k.csv"
                
    with codecs.open(in_file_k, "r",encoding) as fk:
        reader_k = csv.reader(fk)
        for row_k in reader_k:
            data_k = [float(ik) for ik in row_k]
            matrix_k.append(data_k)
    in_file_b = "matrix_b.csv"            
                
    with codecs.open(in_file_b, "r", encoding) as fb:
        reader_b = csv.reader(fb)
        for row_b in reader_b:
            b = [float(ib) for ib in row_b]
            
#SPLITS EACH ROW OF M INTO TIMESTAMP PORTION AND VARIABLE DATA PORTION
    m_ts = []
    for xts in range(len(m)):
        m_ts.append(m[xts][0])        
    m_data = []            
    for x in range(len(m)):
        m_data.append(m[x][1:-1])
    m_reelndx = []
    for xreel in range(len(m)):
        m_reelndx.append(m[xreel][-1])
    
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
    out_header = ['Timestamp',1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,
                  23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,
                  'ReelNdx']    
        
    init = 5

#ACCUMULATOR VARIABLES AND THEIR INDICES. TASK 122 IN VSTS
    '''acc_vars = ['taper1_inlet_tension','taper1_dispenser_diameter',
              'taper1_reel_capacity','taper1_dispenser','taper2_reel_capacity',
              'taper1_outlet_tension','taper2_inlet_tension','taper2_dispenser1',
              'taper2_dispenser2','taper1_dispenser_tension','taper2_dispenser_tension']'''
    acc_vars_num = [23,24,25,26,27,28,29,30,31,36,37]

#ADDS ACCUMULATOR VALUE 
#add outer for loop for 'after certain count' part
    '''count = 200
    for cnt in range(len(count)):'''
    for i4 in range(len(v)):
        for j4 in range(len(v[i4])):
            for acc_index in range(len(acc_vars_num)):
                if j4 == acc_vars_num[acc_index]:
                    v[i4][j4] += init //v = v + init 
                else:
                    pass

#COMBINES TIMESTAMP, INPUT AND OUTPUT VALUES INTO ONE ROW                
    for i3 in range(len(m_data)):
        m_data[i3].extend(v[i3])
        m_data[i3].append(m_reelndx[i3])
        m_data[i3].insert(0,m_ts[i3])
        #insert accumulator values in specified indices

#CREATES MELTED ('UNPIVOTED') DATAFRAME AND SENDS TO A CSV        
    df = pd.DataFrame(data = m_data, columns = out_header)
    melted = pd.melt(df, id_vars = ['Timestamp','ReelNdx'])
    melted.to_csv(outputfile)
               
    '''with codecs.open(outputfile, "a+", encoding) as fq:
        writer = csv.writer(fq)
        writer.writerow(out_header)
        writer.writerows(m_data)'''
    
if __name__ == "__main__":
    main(sys.argv[1:])


