import os

operations = ['++','--','+= ','-=','*=','/=','+','=',' -','/','*','%']
relations = ['<<','>>','<','>','==','!<','!>','!=','<=','>=']


def findIf(str):
    x = str.find("if")
    if(x < 0):
        x = str.find("else")
    return x

def findFor(str):
    x = str.find("for")
    return x

def findExp(str):
    res = any(ele in str for ele in operations)
    if res:
        return 1
    else:
        return 0

def changeFlag(x,str, pair,inside):
    if(findIf(str)>=0):
        inside = 2
        return 2, inside    
    elif(findFor(str)>=0): 
        inside = 3
        return 3, inside
    elif(findExp(str)==1):
        if str[0] != " ":
            return 1, inside
        if len(pair) <= 0 and inside == 0:
            return 1,inside
        else:
            if inside > 1:
                inside = 0
            return x,inside
    else:
        return x,inside

def for_Sort(line_for_statement,i, sorted_for, body):
    if "for" in line_for_statement and body == 0:
        i += 1
        string = "head" + str(i) + ": "
        sorted_for.append(string +line_for_statement)
        if "{" in line_for_statement:
            body = 2
        else:
            body = 1
    elif body == 1 or body == 2:
        string = "head" + str(i) + ":"
        sorted_for.append("body of "+string+line_for_statement)
        if body == 2 and "}" not in line_for_statement:
            body = 2
        else:
            body = 0
        if "for" in line_for_statement:
            body = 0
            sorted_for, i,body = for_Sort(line_for_statement,i,sorted_for, body)
        
    return sorted_for, i,body


def main():
    f = open("input.txt","r")
    data = f.readlines()
    #print(data)

    x = 1
    pair = []
    if_statements = []
    for_statements = []
    exp_statements = []
    t_n = 0
    inside = 0
    for line in data:
        #print(line)
        if "}" in line:
            pair.pop(len(pair)-1)
            print(pair)
        x,inside = changeFlag(x,line, pair,inside)
        if "{" in line:
            pair.append(x)
        if len(pair)>0:
            x = pair[len(pair)-1]
        if (x == 1):
            exp_statements.append(line)
        elif (x==2) or inside == 2:
            if_statements.append(line)
        elif(x==3) or inside == 3:
            for_statements.append(line)

    
    print("expression:\n", exp_statements)
    print("if:\n", if_statements)
    print("for:\n", for_statements)

    sorted_for = []
    body = 0
    i = 0
    for line in for_statements:
        sorted_for,i,body = for_Sort(line,i, sorted_for,body)
    
    print("sortedFor:")
    for line in sorted_for:
        print(line)

main()