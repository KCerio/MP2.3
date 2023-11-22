def find_curly_brace_pairs(code):
    stack = []
    pairs = []

    for i, char in enumerate(code):
        if char == '{':
            stack.append(i)
        elif char == '}':
            if stack:
                start_index = stack.pop()
                pairs.append((start_index, i))
            else:
                # Unmatched '}' found
                print("Unmatched '}' at index {i}")

    # Check for unmatched '{'
    for index in stack:
        print("Unmatched '{' at index {index}")

    return pairs

# Example Python code snippet
f = open('MP2.1_input.txt', 'r')
data = f.readlines()
python_code = "|".join(data)
print(python_code)

brace_pairs = find_curly_brace_pairs(python_code)
print(brace_pairs)

data = python_code.split("|")

inner_loop = []
outer_loop = []
flag = 0
print(data)
o_brace = "{"
c_brace = "}"
for lines in data:
    print("flag",flag)
    if o_brace in lines:
        flag += 1
        continue
    if flag > 0:
        outer_loop.append(lines)
    if flag > 1:
        inner_loop.append(lines)
    if c_brace in lines:
            flag -= 1
    

print("inner loop")
print(inner_loop)
print("outerloop")
print(outer_loop)
#if len(brace_pairs)>1:
    #inner_loop exist
    #inner_loop.append(lines)
#else:
    #outer_loop.append
