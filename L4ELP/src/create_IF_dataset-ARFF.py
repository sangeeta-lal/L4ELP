import MySQLdb
import numpy as np

import utill4


"""=====================================================================================================
@ Author: Sangeeta
@Uses:
1. This file will be used to create dataset from the main training table "project_Training4_IF.java
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
main_source_table = project+"_if_training4"  # from this table we have to take the data
path = "F:\\Research\\L4ELP\\dataset\\"
complete_db_file_path=path+project+"-arff\\if\\complete\\"+project+"_if_complete.arff"
small_balance_db_file_path = path+ project+"-arff\\if\\balance\\"+project+"_if_balance"
"""

port=3307
user="sangeetal"
password="sangeetal"
database="logging4_elp"
main_source_table = project+"_if_training4"  # from this table we have to take the data
path = "E:\\Sangeeta\\Research\\L4ELP\\dataset\\"
complete_db_file_path=path+project+"-arff\\if\\complete\\"+project+"_if_complete.arff"
small_balance_db_file_path = path+ project+"-arff\\if\\balance\\"+project+"_if_balance"
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
    file_obj.write("@attribute is_if_logged {0,1}  "+"\n")    
    file_obj.write("@attribute loc_till_if numeric "+"\n")
    file_obj.write("@attribute is_till_if_logged {0,1} "+"\n")
    file_obj.write("@attribute till_if_log_count numeric "+"\n")
    file_obj.write("@attribute operators_count_till_if numeric "+"\n")
    file_obj.write("@attribute variables_count_till_if numeric "+"\n")
    file_obj.write("@attribute method_call_count_till_if numeric "+"\n")
    file_obj.write("@attribute is_return_in_till_if {0,1} "+"\n")
    file_obj.write("@attribute throw_throws_till_if {0,1} "+"\n")
    file_obj.write("@attribute if_in_till_if {0,1} "+"\n")
    file_obj.write("@attribute if_count_in_till_if numeric "+"\n")
    file_obj.write("@attribute is_assert_till_if {0,1} "+"\n")
    file_obj.write("@attribute is_method_have_param {0,1} "+"\n")
    file_obj.write("@attribute method_param_count numeric "+"\n")
    file_obj.write("@attribute is_return_in_if {0,1} "+"\n")
    file_obj.write("@attribute throw_throws_if {0,1} "+"\n")
    file_obj.write("@attribute is_assert_if {0,1} "+"\n")
    file_obj.write("@attribute is_null_condition_if {0,1} "+"\n")
    file_obj.write("@attribute is_instance_of_condition_if {0,1} "+"\n")
    file_obj.write("@attribute all_text_features_cleaned string "+"\n")
        
    file_obj.write("\n")
    file_obj.write("@data " +"\n")
        
    
#=======================================================#
# @uses: Function to write in file ceate arff files
#=======================================================#
def write_in_file(file_obj, tuple_val):
    
    
    
    t_if_expr                  = tuple_val[0]
    n_loc_till_if              =  tuple_val[1]
    n_is_till_if_logged        = tuple_val[2]
    n_till_if_log_count        = tuple_val[3]
    t_till_if_log_levels       = tuple_val[4]
    t_operators_till_if        = tuple_val[5]
    n_operators_count_till_if  = tuple_val[6]
    t_variables_till_if          = tuple_val[7]
    n_variables_count_till_if    = tuple_val[8]
    t_method_call_names_till_if   =tuple_val[9]
    n_method_call_count_till_if   = tuple_val[10]
    n_is_return_in_till_if        =tuple_val[11]
    n_throw_throws_till_if        =tuple_val[12]
    n_if_in_till_if               =tuple_val[13]
    n_if_count_in_till_if         =tuple_val[14]
    n_is_assert_till_if          =tuple_val[15]
    n_is_method_have_param        =tuple_val[16] 
    t_method_param_type          =tuple_val[17]
    t_method_param_name         =tuple_val[18]
    n_method_param_count        =tuple_val[19]
    n_is_return_in_if           = tuple_val[20]
    n_throw_throws_if          = tuple_val[21]
    n_is_assert_if              =tuple_val[22]
    n_is_null_condition_if          = tuple_val[23] 
    n_is_instance_of_condition_if = tuple_val[24] 
    t_package_name               =tuple_val[25]
    t_class_name                =tuple_val[26]
    t_method_name                =tuple_val[27]
           
    is_if_logged = tuple_val[28]
    
    
    operator_feature =  t_operators_till_if
    
    text_features =      t_if_expr + " "+            t_till_if_log_levels   +" "                  +    t_variables_till_if +" "        +  t_method_call_names_till_if +" "+\
             t_method_param_type + " " +  t_method_param_name +" " +  t_package_name+" "+ t_class_name + " "+ t_method_name         
    
    #Applying camel casing
    text_features = utill4.camel_case_convert(text_features)
    text_features = utill4.remove_stop_words(text_features)
    text_features = utill4.stem_it(text_features)
    
    text_features =  text_features +" " + operator_feature
    
    text_features =  text_features.strip()
    
    print "writing if:"   
   
    
    #=== write the data in the file=====================#
    write_str =""+ (str)(is_if_logged )+","+  (str)(n_loc_till_if)  +","+ (str)(n_is_till_if_logged ) +","+ (str)(n_till_if_log_count) +","+(str)( n_operators_count_till_if) +","+ \
    (str)(n_variables_count_till_if) +","+ (str)( n_method_call_count_till_if)  +","+ (str)(n_is_return_in_till_if)+","+ (str)(n_throw_throws_till_if)  +","+ \
    (str)(n_if_in_till_if) +","+ (str)(n_if_count_in_till_if) +","+ (str)(n_is_assert_till_if ) +","+  (str)(n_is_method_have_param )      +","+ \
    (str)( n_method_param_count)  +","+ (str)(n_is_return_in_if ) +","+ (str)(n_throw_throws_if)  +","+    (str)( n_is_assert_if  )           +","+ \
    (str)(n_is_null_condition_if)  +","+    (str)( n_is_instance_of_condition_if) +",'"+ text_features+"')"
      
    # ==write in the file======#  
    file_obj.write(write_str+"\n")       
            
    #target.append(0)  Removing from here moving up                  
    #db1.commit()           
    
    


def create_one_complete_file(complete_db_file_path):
    #===========Read all the if blocks===============================#
   
   

    str_total_data = "select  if_expr, loc_till_if, is_till_if_logged, till_if_log_count, till_if_log_levels, operators_till_if, operators_count_till_if, variables_till_if,  \
                       variables_count_till_if,method_call_names_till_if, method_call_count_till_if,  is_return_in_till_if, throw_throws_till_if, \
                       if_in_till_if, if_count_in_till_if, is_assert_till_if, is_method_have_param,  method_param_type, method_param_name, method_param_count,\
                       is_return_in_if, throw_throws_if, is_assert_if, is_null_condition_if, is_instance_of_condition_if, package_name, class_name, method_name, is_if_logged\
                       from "+ main_source_table +" where if_expr not like '%isTraceEnabled()'  and \
                       if_expr not like '%isDebugEnabled()'  and if_expr not like '%isInfoEnabled()' and if_expr not like '%isWarnEnabled()'  \
                       and if_expr not like '%isErrorEnabled()'  and if_expr not like '%isFatalEnabled()'  and if_expr!='' "
    
    
    select_cursor.execute(str_total_data)
    total_data = select_cursor.fetchall()


    #===========================================#
    #@ 1. Create the complete database    
    #===========================================#
   
    file_obj =  open(complete_db_file_path, 'w+')
   
    # 1. Write header in the file
    relation_name =  project +"_if_complete"
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
        
  
    
        #===========Read all the logged if blocks===============================#
        str_logged_data = "select  if_expr, loc_till_if, is_till_if_logged, till_if_log_count, till_if_log_levels, operators_till_if, operators_count_till_if, variables_till_if,  \
                       variables_count_till_if,method_call_names_till_if, method_call_count_till_if,  is_return_in_till_if, throw_throws_till_if, \
                       if_in_till_if, if_count_in_till_if, is_assert_till_if, is_method_have_param,  method_param_type, method_param_name, method_param_count,\
                       is_return_in_if, throw_throws_if, is_assert_if, is_null_condition_if, is_instance_of_condition_if, package_name, class_name, method_name, is_if_logged\
                       from "+ main_source_table +" where if_expr not like '%isTraceEnabled()'  and \
                       if_expr not like '%isDebugEnabled()'  and if_expr not like '%isInfoEnabled()' and if_expr not like '%isWarnEnabled()'  \
                       and if_expr not like '%isErrorEnabled()'  and if_expr not like '%isFatalEnabled()'  and if_expr!=''  and is_if_logged=1"
       
        select_cursor.execute(str_logged_data)
        logged_data = select_cursor.fetchall()
        
        logged_data_count =  len(logged_data)
        
        #===================================#
       
        #==open file ===#
        global small_balance_db_file_path
        file_path =  small_balance_db_file_path+"_"+(str)(i+1)+".arff"        
        file_obj =  open(file_path, 'w+')
        
        #====  1. Write Header============= #
        relation_name =  project +"_if_balance_"+(str)(i+1)
        write_header(file_obj, relation_name)
    
        
        #====  2. Write Instances=============#
        for d in logged_data:   
            write_in_file(file_obj, d)   
            
                     
            
        str_non_logged_data = "select  if_expr, loc_till_if, is_till_if_logged, till_if_log_count, till_if_log_levels, operators_till_if, operators_count_till_if, variables_till_if,  \
                       variables_count_till_if,method_call_names_till_if, method_call_count_till_if,  is_return_in_till_if, throw_throws_till_if, \
                       if_in_till_if, if_count_in_till_if, is_assert_till_if, is_method_have_param,  method_param_type, method_param_name, method_param_count,\
                       is_return_in_if, throw_throws_if, is_assert_if, is_null_condition_if, is_instance_of_condition_if, package_name, class_name, method_name, is_if_logged\
                       from "+ main_source_table +" where if_expr not like '%isTraceEnabled()'  and \
                       if_expr not like '%isDebugEnabled()'  and if_expr not like '%isInfoEnabled()' and if_expr not like '%isWarnEnabled()'  \
                       and if_expr not like '%isErrorEnabled()'  and if_expr not like '%isFatalEnabled()'  and if_expr!='' and is_if_logged=0"    
         
  
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

