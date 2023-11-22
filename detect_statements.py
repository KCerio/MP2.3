import os
import re

operations = ['+','=','-','/','*','%','>>','++','--']
relations = ['< ','> ','==','!<','!>','!=','<=','>=']

def extract_arithmetic_statements(cpp_code:str):
    # look for '=' and note its position, 
    # then note everything before and after it as expr 
    pos = 0
    var = ''
    datatypes = ["int", "char"]
    assignment = []
    conditional = []
    input = []
    statement = cpp_code.split(';')
    for state in statement: #lines of cpp_code
        state = state.strip() # erases trailing spaces
        sub = state.split(',') # splits the lines to statements
        for s in sub: 
            s = s.strip()
            ind = 0
            if s >= "0" and s <= "9":
                break
            if '=' in s and 'if' not in s and 'for' not in s:
                # checking for datatypes in the line
                for item in datatypes:
                    ind = s.find(item)
                    if(ind >= 0):
                        ind = ind + len(item)
                        break
                if ind == -1:
                    ind = 0
                # looking for assignment symbol ('=')
                pos = s.index('=')
                # assigning string preceeding ('=') as the variable
                var = s[ind:pos]
                print("var:",var)
                # assigning string after ('=') as the expression
                expr = s[pos+1:len(cpp_code)-1]
                print("expr:", expr)
            elif "cin" in s:
                print(s)
            
            




def main():
    f = open('MP2.1_sampleInputs.txt','r')
    data = f.readlines()

    for lines in data:
        if "input" in lines:
            print(lines)
        else:
            extract_arithmetic_statements(lines)


main()
