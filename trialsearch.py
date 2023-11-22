import re

operations = ['+','=','-','/','*','%','++','--','+=','-=','*=','/=']
relations = ['<','>','==','!<','!>','!=','<=','>=']

def detect_if_statements_and_for_loops(cpp_code):
    # Regular expressions to match if-statements and for-loops
    if_pattern = r'\bif\s*\('
    for_pattern = r'\bfor\s*\('

    # Search for if-statements and for-loops
    if_matches = re.findall(if_pattern, cpp_code)
    for_matches = re.findall(for_pattern, cpp_code)

    # Determine if-statements and for-loops based on the matches
    if_present = len(if_matches) > 0
    for_present = len(for_matches) > 0

    return if_present, for_present


def counter(str,flag):
    #print("flag ",flag)
    #print("str: ", str)
    #if str[0] == " ":
     #   str[0].replace(" ", "")
    str = str.strip()
    if len(str)<=2:
        return 0
    str = str.split(";") 
    str = str[0]
    substring = ','
    if flag == 0:
        if substring in str:
            y = str.split(",")
            #print("here")
            for word in y:
                i = 0
                cha = word.split()
                for op in cha:
                    if op in operations:
                        print(cha[i-1],cha[i],cha[i+1])
                    i+=1  
                continue
            return 0
        print(str)
    #elif flag == 1:
     #   if_statement(str)
    elif flag == 2:
        print("for statement: ", str)
        if "}" in str:
            flag = 0
    
    return flag

def statement(str, list):
    list.append(str)
    return list


def if_statement(if_list):
    i=0
    count = 0
    for statement in if_list:
        if i == 0:
            condition = statement.split("(")
            for tok in condition:
                for op in tok:
                    if op in relations:
                        print("condition: ",tok[0:len(tok)-2])
                    #print("condition: ", tok[j-1],tok[j],tok[j+1])
            
        else:
            statement = statement.strip()
            statement = statement[0:len(statement)-1]
            if i == 1:
                print("if statements: ")
                print(statement)
                count += 1
            else:
                print(statement)
        i+=1

def for_loop(str):
    for_statements = str.split("{")
    if for_statements[0]:
        state = for_statements[0].split(";")
        ##print("state: ",state)
        if state[0]:
            initializer = state[0]
            #print(initializer)
            i=0
            for op in initializer:
                if op in operations:
                    print("initializer: ",initializer[i-1], initializer[i],initializer[i+1])
                i+=1
        condition = state[1]
        print("condition: ", condition)
        if state[2]:    
            update = state[2]
            print("update: ", update[0: len(update)-1])

    if for_statements[1]:
        statement = for_statements[1].split(";")
        #print("state: ", statement)
        for state in statement:
            for x in state:
                if x in operations:
                    state = state.strip()
                    print(state)
            
        

# Example C++ code snippet

f = open('MP2.1_input.txt', 'r')
data = f.readlines()
flag = 0
if_list = []
for_list = []
if_key = 0
for_key = 0

for line in data:   
    cpp_code = line
    if_present, for_present = detect_if_statements_and_for_loops(cpp_code)
    if cpp_code == " ":
        data.remove(line)
        continue

    if if_present or flag == 1:
        if_key = 1
        flag = 1
        #print(cpp_code)
        if_list = statement(cpp_code,if_list)


    if for_present or flag == 2:
        for_key = 1
        flag = 2
        #print("for loop: ",cpp_code)
        #counter(cpp_code,flag)
        for_list = statement(cpp_code,for_list)
        #print(for_list)
        
    else:
        ##print(cpp_code)
        counter(cpp_code,flag)

if if_key == 1:
    print("if: ")
    print(if_list)
    if_statement(if_list)

if for_key == 1:
    print("for:")
    for_string = "".join(for_list)
    print(for_string)
    for_loop(for_string)

    





#you have to find a job to sustain your ... art(?)
#walay manunulat na mabuhi sa sulat lang

#cpp_code = " ".join(data)


