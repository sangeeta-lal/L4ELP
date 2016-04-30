import MySQLdb
import numpy as np

import utill4


"""=====================================================================================================
@ Author: Sangeeta
@Uses:
1. This file will be used to create dataset from the main training table "project_Training4_Catch.java
2. It will create 11 ARFF Files

    a. One having all the instances present in the main table
    b. 10 balanced ARFF having equal number of logged and non-logged instances
======================================================================================================"""

#Project
#"""
project= "tomcat"
title = 'Tomcat'
#"""
"""
project =  "cloudstack"
title = 'CloudStack'
#"""

"""
project =  "hd"
title = 'Hadoop'
#"""

#"""
port=3306
user="root"
password="1234"
database="logging4_elp"
main_source_table = project+"_catch_training4"  # from this table we have to take the data
path = "F:\\Research\\L4ELP\\dataset\\"
complete_db_file_path=path+project+"-arff\\catch\\complete\\"+project+"_catch_complete.arff"
small_balance_db_file_path = path+ project+"-arff\\catch\\balance\\"+project+"_catch_balance"
"""

port=3307
user="sangeetal"
password="sangeetal"
database="logging4_elp"
main_source_table = project+"_catch_training4"  # from this table we have to take the data
path = "E:\\Sangeeta\\Research\\L4ELP\\dataset\\"
complete_db_file_path=path+project+"-arff\\catch\\complete\\"+project+"_catch_complete.arff"
small_balance_db_file_path = path+ project+"-arff\\catch\\balance\\"+project+"_catch_balance"
#"""



db1= MySQLdb.connect(host="localhost",user=user, passwd=password, db=database, port=port)
select_cursor = db1.cursor()
insert_cursor = db1.cursor()



#=======================================================#
#  @Uses:Write_header() is a function that is used toinsert
# the ARFF header in the file.
#=======================================================#
def write_header(file_obj,relation_name):
    
    file_obj.write("@relation    "  + relation_name+"\n" )
    file_obj.write("@attribute is_catch_logged {0,1}  "+"\n")
    file_obj.write("@attribute try_loc numeric "+"\n")
    file_obj.write("@attribute is_try_logged {0,1}"+"\n")
    file_obj.write("@attribute try_log_count numeric "+"\n")
    file_obj.write("@attribute have_previous_catches {0,1} "+"\n")
    file_obj.write("@attribute previous_catches_logged {0,1} "+"\n")
    file_obj.write("@attribute is_return_in_try {0,1} "+"\n")
    file_obj.write("@attribute is_return_in_catch {0,1} "+"\n")
    file_obj.write("@attribute is_catch_object_ignore {0,1} "+"\n")
    file_obj.write("@attribute is_interrupted_exception {0,1} "+"\n")
    file_obj.write("@attribute is_thread_sleep_try {0,1} "+"\n")
    file_obj.write("@attribute throw_throws_try {0,1} "+"\n")
    file_obj.write("@attribute throw_throws_catch {0,1} "+"\n")
    file_obj.write("@attribute if_in_try {0,1} "+"\n")
    file_obj.write("@attribute if_count_in_try numeric "+"\n")
    file_obj.write("@attribute is_assert_in_try {0,1} "+"\n")
    file_obj.write("@attribute is_assert_in_catch {0,1} "+"\n")
    file_obj.write("@attribute is_method_have_param {0,1} "+"\n")
    file_obj.write("@attribute method_param_count numeric "+"\n")
    file_obj.write("@attribute method_call_count_try numeric "+"\n")
    file_obj.write("@attribute operators_count_in_try numeric "+"\n")
    file_obj.write("@attribute variables_count_try numeric "+"\n")
    file_obj.write("@attribute method_call_count_till_try numeric "+"\n")
    file_obj.write("@attribute operators_count_till_try numeric "+"\n")
    file_obj.write("@attribute variables_count_till_try numeric "+"\n")
    file_obj.write("@attribute loc_till_try numeric "+"\n")
    file_obj.write("@attribute is_till_try_logged {0,1} "+"\n")
    file_obj.write("@attribute till_try_log_count numeric "+"\n")
    file_obj.write("@attribute is_return_till_try {0,1} "+"\n")
    file_obj.write("@attribute throw_throws_till_try {0,1} "+"\n")
    file_obj.write("@attribute if_in_till_try {0,1} "+"\n")
    file_obj.write("@attribute if_count_in_till_try numeric "+"\n")
    file_obj.write("@attribute is_assert_till_try {0,1} "+"\n")
    file_obj.write("@attribute all_text_feature_cleaned string "+"\n")
    
    file_obj.write("\n")
    file_obj.write("@data " +"\n")
        
    
#=======================================================#
# @uses: Function to write in file ceate arff files
#=======================================================#
def write_in_file(file_obj, tuple_val):
    
    t_catch_exc     = tuple_val[0]
    t_package_name  = tuple_val[1]
    t_class_name    = tuple_val[2]
    t_method_name   = tuple_val[3]

    n_try_loc       = tuple_val[4]
    n_is_try_logged = tuple_val[5]
    n_try_log_count  =tuple_val[6]

    t_try_log_levels = tuple_val[7]

    n_have_previous_catches=tuple_val[8]
    n_previous_catches_logged =tuple_val[9]
    n_is_return_in_try =tuple_val[10]                     
    n_is_return_in_catch  = tuple_val[11]
    n_is_catch_object_ignore = tuple_val[12]
    n_is_interrupted_exception = tuple_val[13]
    n_is_thread_sleep_try = tuple_val[14]
    n_throw_throws_try = tuple_val[15]                             
    n_throw_throws_catch= tuple_val[16]
    n_if_in_try =tuple_val[17]
    n_if_count_in_try = tuple_val[18]
    n_is_assert_in_try = tuple_val[19]
    n_is_assert_in_catch = tuple_val[20]
    n_is_method_have_param = tuple_val[21]
    
    t_method_param_type = tuple_val[22]
    t_method_param_name = tuple_val[23]

    n_method_param_count = tuple_val[24]
    
    t_method_call_names_try = tuple_val[25]
    
    n_method_call_count_try= tuple_val[26]
    
    t_operators_in_try = tuple_val[27]
    
    n_operators_count_in_try =tuple_val[28]
    
    t_variables_in_try =tuple_val[29]
    
    n_variables_count_try =tuple_val[30]
    
    t_method_call_names_till_try =tuple_val[31]
    
    n_method_call_count_till_try =tuple_val[32]
    
    t_operators_till_try  =tuple_val[33]
    
    n_operators_count_till_try =tuple_val[34]
    
    t_variables_till_try =tuple_val[35]
    
    n_variables_count_till_try =tuple_val[36] 
    n_loc_till_try =tuple_val[37]
    n_is_till_try_logged =tuple_val[38] 
    n_till_try_log_count =tuple_val[39]

    t_till_try_log_levels =tuple_val[40]
    
    n_is_return_till_try =tuple_val[41]
    n_throw_throws_till_try =tuple_val[42]
    n_if_in_till_try =tuple_val[43]
    n_if_count_in_till_try =tuple_val[44] 
    n_is_assert_till_try =tuple_val[45]
   
    try_id    = tuple_val[46]
    catch_id = tuple_val[47]
    is_catch_logged  = tuple_val[48]
    
    
    text_features =      t_catch_exc+ " "+            t_package_name +" "                  + t_class_name+" "        + t_method_name  +" "+\
                 t_method_param_type + " " +  t_method_param_name +" " +            t_method_call_names_try +" " +\
                 t_variables_in_try  +" " +   t_try_log_levels +" "+                  t_method_call_names_till_try +" "+   t_variables_till_try +"  "+\
                 t_till_try_log_levels

    #Applying camel casing
    text_features = utill4.camel_case_convert(text_features)
    text_features =  utill4.remove_stop_words(text_features)
    text_features = utill4.stem_it(text_features)
    
    operator_string =  t_operators_in_try +" "+ t_operators_till_try
    
    text_features =  text_features +" " + operator_string
    
    text_features =  text_features.strip()
    
    print "writing try id=", try_id
    
    #====Insert the data in the table=====#
    write_str =  ""+ (str)(is_catch_logged) +","+ (str)(n_try_loc) +","+ (str)(n_is_try_logged)  +","+ (str)(n_try_log_count)\
    +","+ (str)(n_have_previous_catches) +","+ (str)(n_previous_catches_logged)  +","+ (str)(n_is_return_in_try) +","+ (str)(n_is_return_in_catch) +","+ (str)(n_is_catch_object_ignore) +","+ (str)(n_is_interrupted_exception)\
    +","+ (str)(n_is_thread_sleep_try)  +","+ (str)(n_throw_throws_try)  +","+ (str)(n_throw_throws_catch) +","+ (str)(n_if_in_try) +","+ (str)(n_if_count_in_try) +","+ (str)(n_is_assert_in_try)\
    +","+ (str)(n_is_assert_in_catch) +","+ (str)(n_is_method_have_param) +","+ (str)(n_method_param_count)  +","+ (str)(n_method_call_count_try) +","+ (str)(n_operators_count_in_try)\
    +","+ (str)(n_variables_count_try) +","+ (str)(n_method_call_count_till_try) +","+ (str)(n_operators_count_till_try)\
    +","+ (str)(n_variables_count_till_try) +","+ (str)(n_loc_till_try) +","+ (str)(n_is_till_try_logged) +","+ (str)(n_till_try_log_count)\
    +","+ (str)(n_is_return_till_try) +","+ (str)(n_throw_throws_till_try) +","+ (str)(n_if_in_till_try) +","+ (str)(n_if_count_in_till_try)\
    +","+ (str)(n_is_assert_till_try)  +",'"+  text_features+"')"
      
    # ==write in the file======#  
    file_obj.write(write_str+"\n")       
                
    
    #target.append(0)  Removing from here moving up                  
    #db1.commit()           
    
    


def create_one_complete_file(complete_db_file_path):
    #===========Read all the catch blocks===============================#
    str_total_data = "select  catch_exc, package_name, class_name, method_name, try_loc, is_try_logged, try_log_count, try_log_levels, have_previous_catches, previous_catches_logged, \
                      is_return_in_try, is_return_in_catch, is_catch_object_ignore, is_interrupted_exception, is_thread_sleep_try,\
                       throw_throws_try,  throw_throws_catch, if_in_try, if_count_in_try, is_assert_in_try, is_assert_in_catch, \
                      is_method_have_param, method_param_type, method_param_name, method_param_count, method_call_names_try, \
                      method_call_count_try, operators_in_try, operators_count_in_try, variables_in_try, variables_count_try,\
                      method_call_names_till_try, method_call_count_till_try, operators_till_try, operators_count_till_try, variables_till_try,\
                      variables_count_till_try, loc_till_try, is_till_try_logged, till_try_log_count, till_try_log_levels,is_return_till_try, throw_throws_till_try, \
                     if_in_till_try, if_count_in_till_try,  is_assert_till_try, try_id, catch_id, is_catch_logged  from  "+ main_source_table +" where catch_exc!='' "
   

    select_cursor.execute(str_total_data)
    total_data = select_cursor.fetchall()


    #===========================================#
    #@ 1. Create the complete database    
    #===========================================#
   
    file_obj =  open(complete_db_file_path, 'w+')
   
    # 1. Write header in the file
    relation_name =  project +"_catch_complete"
    write_header(file_obj, relation_name)
    
    #2. write database ibstabces
    for d in total_data:   
        write_in_file(file_obj, d)
    
    
    file_obj.close()

#===========================================#
#@ 2. Create 10 small dataset (balance)
#===========================================#    
def create_10_small_balance_files():

    number_of_small_ds = 10
    for i in range(number_of_small_ds):
        
  
    
        #===========Read all the catch blocks===============================#
        str_logged_data = "select  catch_exc, package_name, class_name, method_name, try_loc, is_try_logged, try_log_count, try_log_levels, have_previous_catches, previous_catches_logged, \
                      is_return_in_try, is_return_in_catch, is_catch_object_ignore, is_interrupted_exception, is_thread_sleep_try,\
                       throw_throws_try,  throw_throws_catch, if_in_try, if_count_in_try, is_assert_in_try, is_assert_in_catch, \
                      is_method_have_param, method_param_type, method_param_name, method_param_count, method_call_names_try, \
                      method_call_count_try, operators_in_try, operators_count_in_try, variables_in_try, variables_count_try,\
                      method_call_names_till_try, method_call_count_till_try, operators_till_try, operators_count_till_try, variables_till_try,\
                      variables_count_till_try, loc_till_try, is_till_try_logged, till_try_log_count, till_try_log_levels,is_return_till_try, throw_throws_till_try, \
                     if_in_till_try, if_count_in_till_try,  is_assert_till_try, try_id, catch_id, is_catch_logged  from  "+ main_source_table +" where catch_exc!=''  and is_catch_logged=1"   
  
        select_cursor.execute(str_logged_data)
        logged_data = select_cursor.fetchall()
        
        logged_data_count =  len(logged_data)
        
        #===================================#
       
        #==open file ===#
        global small_balance_db_file_path
        file_path =  small_balance_db_file_path+"_"+(str)(i+1)+".arff"        
        file_obj =  open(file_path, 'w+')
        
        #====  1. Write Header============= #
        relation_name =  project +"_catch_balance_"+(str)(i+1)
        write_header(file_obj, relation_name)
    
        
        #====  2. Write Instances=============#
        for d in logged_data:   
            write_in_file(file_obj, d)            
            
            
        str_non_logged_data = "select  catch_exc, package_name, class_name, method_name, try_loc, is_try_logged, try_log_count, try_log_levels, have_previous_catches, previous_catches_logged, \
                      is_return_in_try, is_return_in_catch, is_catch_object_ignore, is_interrupted_exception, is_thread_sleep_try,\
                       throw_throws_try,  throw_throws_catch, if_in_try, if_count_in_try, is_assert_in_try, is_assert_in_catch, \
                      is_method_have_param, method_param_type, method_param_name, method_param_count, method_call_names_try, \
                      method_call_count_try, operators_in_try, operators_count_in_try, variables_in_try, variables_count_try,\
                      method_call_names_till_try, method_call_count_till_try, operators_till_try, operators_count_till_try, variables_till_try,\
                      variables_count_till_try, loc_till_try, is_till_try_logged, till_try_log_count, till_try_log_levels,is_return_till_try, throw_throws_till_try, \
                     if_in_till_try, if_count_in_till_try,  is_assert_till_try, try_id, catch_id, is_catch_logged  from  "+ main_source_table +" where catch_exc!=''  and is_catch_logged=0"   
  
        select_cursor.execute(str_non_logged_data)
        non_logged_data = select_cursor.fetchall()
        
     
            
        np.random.seed(i)
        indices = list()
        indices = np.random.permutation(len(non_logged_data))[:len(logged_data)]

        print "len not logged tuples=", len(non_logged_data), " indices len=", len(indices)

        valid_index=-1

        for d in non_logged_data:
   
            valid_index= valid_index+1
            if valid_index in indices: 
                write_in_file(file_obj, d)    
                       
        
        file_obj.close()  

#=========== Run ========================#


create_one_complete_file(complete_db_file_path)
create_10_small_balance_files()

