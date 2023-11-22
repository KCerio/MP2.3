import os
import re

operations = ['+','=','-','/','*','%','>>','++','--']
relations = ['< ','> ','==','!<','!>','!=','<=','>=']

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

def extract_arithmetic_statements(cpp_code):
    # Define a regular expression pattern to find arithmetic expressions
    pattern = r'([a-zA-Z_]\w*\s*=\s*[0-9+\-*/()\s]+;)'

    # Find all matches in the C++ code
    matches = re.findall(pattern, cpp_code)

    return matches

def operation_check(str):
    for op in operations:
        if op in str:
            return 1
    return 0

def relation_check(str):
    for op in relations:
        if op in str:
            return 1
    return 0


def print_statement(key,statement):
    if key == 0 and len(statement) > 1:
        print("statements:")
        for line in statement:
            line = line.strip()
            #print(line)
            statements = line.split(';')
            for state in statements:
                state = state.strip()
                if operation_check(state) == 0:
                    continue
                else:
                    if "," in state:
                        expression = state.split(",")
                        for each in expression:
                            each = each.split()
                            for var in each:
                                if var in operations:
                                    i = each.index(var)
                                    print(each[i-1], each[i], each[i+1])
                    else:
                        state = state.strip()
                        print(state)
            
                        
    elif key == 1:
        statements_if = []
        else_statements = []
        else_key = 0
        print("if:")
        for line in statement:
            if relation_check(line) == 1:
                line = line.split('(')
                condition = line[1]
                c = 0
                for c in range(0,len(condition)-1):
                    if condition[c] == '}' or condition[c] == ')':
                        break
                    c+=1
                condition = condition[0:c]
                condition = condition.strip()
                print("condition:",condition)
            else:
                if "else" in line:
                    else_key = 1
                if operation_check(line) == 0:
                    continue
                else:
                    if else_key == 1:
                        line = line.replace(";","")
                        else_statements.append(line)
                    else:
                        line = line.replace(";","")
                        statements_if.append(line)
        print("if statements:")
        for line in statements_if:
            line = line.strip()
            print(line)
        if len(else_statements) > 0:
            print("else statements:")
            for line in else_statements:
                line = line.strip()
                print(line)


    elif key == 2:
        statement_for = []
        print("for:")
        for line in statement:
            if "for" in line:
                line = line.strip()
                line = line.split(";")
                initializer = line[0]
                initializer = initializer.replace(" ","")
                print("initializer:", initializer[len(initializer)-3],initializer[len(initializer)-2],initializer[len(initializer)-1])
                condition = line[1]
                condition = condition.strip()
                print("condition:", condition)
                update = line[2].strip()
                update = update[0:3]
                print("update:", update)
            else:
                line = line.split(";")
                for expression in line:
                    if operation_check(expression) == 1:
                        expression = expression.strip()
                        statement_for.append(expression)
        print("for statements:")
        for line in statement_for:
            print(line)




def tokenize(list):
    for line in list:
        if "for" in line:
            #d = "{"
            #line =  [e+d for e in line.split(d) if e]
            line = line.split("{")
            line[0] = line[0] + '{\n'
            #print(line)
        else:
            line = line.split(";")
    return line

f = open('input.txt', 'r')
data = f.readlines()
statements = []
if_statements = []
for_statements = []
key_list = []
i = 0
flag = 0
key = 0

if len(data) == 2:
    data = tokenize(data)

for lines in data:
    prev_key = key
    """i+=1
    if i == 1:
        continue"""
    cpp_code = lines
    if_present, for_present = detect_if_statements_and_for_loops(cpp_code)
    if len(key_list) != 0:
        key = key_list[len(key_list)-1]
    elif len(key_list) == 0 and lines[0] != " ":
        key = 0
    if if_present or key == 1:
        key = 1            
        if_statements.append(lines)
    elif "else" in lines:
        key = 1
        if_statements.append(lines)
    elif for_present or key ==2:
        key = 2
        for_statements.append(lines)
    elif key == 0:
        key = 0
        statements.append(lines)
    #print("key ", key)
    if "{" in lines:
        key_list.append(key)
        #print(key_list)
    elif "}" in lines:
        key_list.pop()
    if prev_key != key:
        if prev_key == 0:
            print_statement(0,statements)
            statements.clear()
        elif prev_key == 1:
            print_statement(1,if_statements)
            if_statements.clear()
        elif prev_key == 2:
            print_statement(2,for_statements)
            for_statements.clear()
    #print("key ", key)

if key == 0:
    print_statement(0,statements)
elif key == 1:
    print_statement(1,if_statements)
elif key == 2:
    print_statement(2,for_statements)