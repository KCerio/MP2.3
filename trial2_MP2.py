import os

operations = ['++','--','+= ','-=','*=','/=','+','=','-','/','*','%']
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

def changeFlag(x,str, pair):
    if(findIf(str)>=0):
        return 2    
    elif(findFor(str)>=0): 
        return 3
    elif(findExp(str)==1):
        if str[0] != " ":
            return 1
        elif len(pair) <=0 :
            return 1
        else:
            return x
    else:
        return x
        
def count(str):
    res1 = any(ele in str for ele in operations)
    res2 = any(ele in str for ele in relations)
    x=0
    if res1:
        for op in operations:
            if '= -' in str:  #not include negative values
                x = 1
                break
            x = x + str.count(op)
            if str.count(op)>0:
                print(op,str.count(op))
                str = str.replace(op,"_")
            
            
    elif res2:
        for re in relations:
            if re in str:
                x = x + str.count(re)
                if str.count(re)>0:
                    print(re,str.count(re))
                    str = str.replace(re,"_")
    print(x)
    return x

def get_lb_ub_incre(init, condition, incre):
    log_flag = 0
    frac_flag = 0
    i = 0
    for c in incre:
        if c == '+':
            if incre[i+1] == '+':
                increment = '+1'
            elif incre[i+1] == '=':
                frac_flag = 1
                increment = '+' + incre[i+2:len(incre)-2]
        elif c == '-':
            if incre[i+1] == '-':
                increment = '-1'
            elif incre[i+1] == '=':
                frac_flag = 1
                increment = '+' + incre[i+2:len(incre)]
        elif c == '*':
            log_flag = 1
            increment = '*' + incre[i+2:len(incre)-2]
        elif c == '/':
            log_flag = 2
            increment = '/' + incre[i+2:len(incre)-2]
        i+=1
    i = 0
    for c in init:
        if c == '=':
            lb = init[i+1 : len(init)]
            if frac_flag == 1 or log_flag == 1:
                lb = '0'
        i+=1
    i = 0
    power = 1
    for c in condition:
        if c == '*':
            power +=1
        if c == '<':
            if condition[i+1] == '=':
                ub = condition[i+2:len(condition)].strip()
                if power > 1:
                    if power == 2:
                        ub = 'sqrt(' + ub +')'
                    elif power == 3:
                        ub = 'cubert(' + ub + ')'
            else:
                condition = condition[i+1:len(condition)].strip()
                ub = condition + '-1'
        i+=1
    
    if log_flag == 1:
        ub = 'log(' + increment[len(increment)-1] +') ' + ub
    elif log_flag == 2:
        ub = 'Log(' + increment[len(increment)-1] +') ' + lb.strip()
        lb = 0
    if frac_flag == 1:
        ub = ub + '\/' + increment[len(increment)-1]

    print("New: ",lb, ub, increment)
    return lb, ub, increment

def checkBounds(lb,ub,incre):
    lb = lb.strip()
    if lb.isalpha() and ub.isdigit():
        return 1 #either lb >= ub
    else:
        lb = 1
        ub = 10
        i = int(incre[1])
        for i in range(1,10):
            if '-' in incre:
                lb -= i
            elif '+' in incre:
                lb += i
            elif '*' in incre:
                lb *= i
            elif '/' in incre:
                lb /= i

        if lb < ub and lb > 0:
            return 0 #goes inside loop
        else:
            return 2 #infinite
     

def switch(key, statements):
    n = 0
    if key == 1:
        i = -1
        for exp in statements:
            i+=1
            if "," in exp:
                statements.pop(i)
                exp = exp.split(",")
                for phrase in exp:
                    statements.insert(i,phrase)
                    i+=1
            else:
                continue
        print("expression:\n", statements)
                
        for exp in statements:
            n = n + count(exp)
        print(n)
        return n
    
    elif key == 2:
        for line in statements:
            n = n + count(line)
        print(n)
        return n
    elif key == 3:
        statements = " ".join(statements)
        statements = statements.split("{")
        #print(statements)
        header = statements[0]
        body = statements[1]
        header = header.split(";")
        init = header[0]
        condition = header[1]
        incre = header[2]
        print("outside")
        outside_loop = n + count(init) + count(condition)
        print("inside")
        inside_loop = n + count(condition) + count(incre) + count(body)
        print(outside_loop, inside_loop)
        lb, ub, incre = get_lb_ub_incre(init,condition, incre)
        if ub.isdigit() or ub.isalpha():
            if checkBounds(lb,ub,incre) == 0:
                n = loop_summation(inside_loop,lb,ub, outside_loop, incre)
            elif checkBounds(lb,ub,incre) == 1:
                n = outside_loop
            elif checkBounds(lb,ub,incre) == 2:
                n = 'infinite'
        else:
            if '/' in incre:
                n = outside_loop
            else:
                n = loop_summation(inside_loop,lb,ub, outside_loop, incre)
        return n

def simplify(string):
    string = string.split(" ")
    cons = 0
    a = []
    print(string)
    for x in string:
        if x.isdigit():
            if string[string.index(x)-1] == '-':
                x = '-'+ x
            cons += int(x)
    
    for x in string:
        if (x == '-' or x == '+' and string[string.index(x)+1].isdigit()) or x.isdigit():
            #print(x)
            continue
        a.append(x + ' ')

    print("A: ", a)
    if len(a) >0:
        if cons > 0:
            constant = '+ '+ str(cons)
            a.append(constant)
        else:
            constant = '- '+ str(abs(cons))
            a.append(constant)
        ans = "".join(a)
    else:
        ans = str(cons)
    return ans


def loop_summation(t_count, lb, ub, f_count, incre):
    print(t_count, lb, ub, f_count, incre)
    #print(type(t_count), type(lb), type(ub), type(f_count), type(incre))
    #formula = ub + lb + '+1'
    ub = ub.strip()
    if ub.isdigit():
        ans = str(t_count * int(ub))
    else:
        for c in ub:
            if "log" in ub or 'sqrt' in ub or 'cubert' in ub:
                ans = str(t_count) + "_" +ub
                break
            elif '\/' in ub:
                ans = str(t_count) + ub
                break
            else:
                if c.isalpha():
                    ans = str(t_count) + c
                elif c == '+':
                    ans = ans + ' + ' + str(t_count*int(ub[len(ub)-1]))
                elif c == '-':
                    ans = ans + ' - ' + str(t_count*int(ub[len(ub)-1]))

    lb = lb.strip()
    if lb.isdigit():
       ans = ans +' - '+ str(t_count * int(lb))
    else:
        for c in lb:
            if c == '0':
                break
            else:
                if c.isalpha():
                    ans = ans +' -'+ str(t_count) + c
                elif c == '-':
                    ans = ans + ' + ' + str(t_count*int(lb[len(lb)-1]))
                elif c == '+':
                    ans = ans + ' - ' + str(t_count*int(lb[len(lb)-1]))
    
    ans = ans + ' + ' +str(t_count + f_count)
    ans = simplify(ans)
    if "_" in ans:
        ans = ans.replace("_"," ")
    if "\/" in ans:
        ans = ans.replace("+","\+")
        ans = "T\(n\) = "+ str(t_count) + ub + " \+ "+str(f_count) + "|T\(n\) = " + ans
    
    for term in ans:
        if '-' in term:
            orig_term = term
            term = term.replace('-', '- ')
            ans = ans.replace(orig_term,term)

    return ans

def main():
    f = open("input.txt","r")
    data = f.readlines()
    #print(data)

    x = 1
    pair = []
    if_statements = []
    for_statements = []
    exp_statements = []
    key_list=[]
    t_n = 0
    
    for line in data:
        #print(line)
        if "}" in line:
            pair.pop(len(pair)-1)
            print(pair)
        x = changeFlag(x,line, pair)
        if "{" in line:
            pair.append(x)
        if len(pair)>0:
            x = pair[len(pair)-1]
        if (x == 1):
            exp_statements.append(line)
        elif (x==2):
            if_statements.append(line)
        elif(x==3):
            for_statements.append(line)

    print("expression:\n", exp_statements)
    print("if:\n", if_statements)
    print("for:\n", for_statements)
    
    if len(exp_statements)>0:
        t_n = t_n + switch(1,exp_statements)
    if len(if_statements)>0:
        t_n = t_n + switch(2,if_statements)
    if len(for_statements)>0:
        t_n = switch(3,for_statements)

    if type(t_n) == str and '\/' in t_n:
        print(t_n)
    else:
        print("T(n) =",t_n)
        #print(line)
    #print(findIf("there is if"))
    #print(findFor("there is for"))


main()