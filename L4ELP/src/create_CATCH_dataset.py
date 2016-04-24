import MySQLdb
import numpy as np

import utill4


"""=====================================================================================================
@ Author: Sangeeta
@Uses:
1. This file will be used to create dataset from the main training table "project_Training4_Catch.java
2. It will create 11 tables (datasets)

    a. One having all the instances present in the main table
    b. 10 balanced table having only 
======================================================================================================"""

#Project
#"""
project= "tomcat_"
title = 'Tomcat'
#"""
"""
project =  "cloudstack_"
title = 'CloudStack'
#"""

"""
project =  "hd_"
title = 'Hadoop'
#"""

#"""
port=3306
user="root"
password="1234"
database="logging4_elp"
main_source_table = project+"catch_training4"  # from this table we have to take the data
insert_table_complete=project+"catch_complete"
insert_table_balance = project+"catch_balance"
#"""

print "insert_table", insert_table_balance

db1= MySQLdb.connect(host="localhost",user=user, passwd=password, db=database, port=port)
select_cursor = db1.cursor()
insert_cursor = db1.cursor()



#=======================================================#
# @uses: Function to insert data in the table
#=======================================================#
def insert_in_table(insert_table_name, tuple_val):
    
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
    
    print "inserting try id=", try_id
    
    #====Insert the data in the table=====#
    insert_str =  "insert into  "+ insert_table_name + " values ("+ (str)(try_id) +","+(str)(catch_id) +","+ (str)(is_catch_logged) +","+ (str)(n_try_loc) +","+ (str)(n_is_try_logged)  +","+ (str)(n_try_log_count)\
    +","+ (str)(n_have_previous_catches) +","+ (str)(n_previous_catches_logged)  +","+ (str)(n_is_return_in_try) +","+ (str)(n_is_return_in_catch) +","+ (str)(n_is_catch_object_ignore) +","+ (str)(n_is_interrupted_exception)\
    +","+ (str)(n_is_thread_sleep_try)  +","+ (str)(n_throw_throws_try)  +","+ (str)(n_throw_throws_catch) +","+ (str)(n_if_in_try) +","+ (str)(n_if_count_in_try) +","+ (str)(n_is_assert_in_try)\
    +","+ (str)(n_is_assert_in_catch) +","+ (str)(n_is_method_have_param) +","+ (str)(n_method_param_count)  +","+ (str)(n_method_call_count_try) +","+ (str)(n_operators_count_in_try)\
    +","+ (str)(n_variables_count_try) +","+ (str)(n_method_call_count_till_try) +","+ (str)(n_operators_count_till_try)\
    +","+ (str)(n_variables_count_till_try) +","+ (str)(n_loc_till_try) +","+ (str)(n_is_till_try_logged) +","+ (str)(n_till_try_log_count)\
    +","+ (str)(n_is_return_till_try) +","+ (str)(n_throw_throws_till_try) +","+ (str)(n_if_in_till_try) +","+ (str)(n_if_count_in_till_try)\
    +","+ (str)(n_is_assert_till_try)  +",'"+  text_features+"')"
      
    print "insert str",insert_str                 
    insert_cursor.execute(insert_str)                
                
    
    #target.append(0)  Removing from here moving up                  
    db1.commit()           
    
    


def create_one_complete_ds():
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
    for d in total_data:   
        insert_in_table(insert_table_complete, d)
    

#===========================================#
#@ 2. Create 10 small dataset
#===========================================#    
def create_10_small_ds():

    number_of_small_ds = 10
    for i in range(number_of_small_ds):
        
        global insert_table_balance     
        new_table    =  insert_table_balance+"_"+(str)(i+1)
    
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
        for d in logged_data:   
            insert_in_table(new_table, d)            
            
            
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
                insert_in_table(new_table, d)    
                       


#create_one_complete_ds()
create_10_small_ds()

