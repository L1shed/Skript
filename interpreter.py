import regex as re

def arrVal(arr, i):
    try:
        return arr[i]
    except IndexError:
        return 'null'

def lexer(contents):
    lines = contents.split('\n')

    nLines = []
    for line in lines:
        chars = list(line)
        temp_str = ""
        tokens = []
        quote_count = 0
        for char in chars:
            if char == '"' or char == "'":
                quote_count += 1
            if quote_count % 2 == 0:
                in_quotes = False
            else:
                in_quotes = True
            if char == " " and in_quotes == False:
                tokens.append(temp_str)
                temp_str = ""
            else:
                temp_str += char
        tokens.append(temp_str)
        items = []
        for token in tokens:
            if token[0] == "'" or token[0] == '"':
                if token[-1] == '"' or token[-1] == "'":
                    items.append(("string", token))
                else:
                    # Throw Error
                    break
            elif re.match(r"[.0-9]+", token):
                items.append(("number", token))
            elif re.match(r"[.a-zA-Z]+", token):
                items.append(("symbol", token))
            elif token in "+-*/=":
                items.append(("expression", token))
            else:
                items.append(("symbol", token))
            
        nLines.append(items)
    return nLines

# All Symbols. Anything not in here  is considered a variable
Symbols = [
    "set",
    "function",
    "send"
]

Vars = {
    
}

def parse(file):
    contents = open(file, 'r').read()
    lines = lexer(contents)
    for i in range(len(lines)):
        line = lines[i]
        inst_line = ""
        for y in range(len(line)):
            token = line[y] # (type, value)
            tokenVal = token[1]
            if token[0] == 'symbol':
                if tokenVal in Symbols:
                    if tokenVal == 'set':
                        inst_line += 'Vars["$v"]'
                    elif tokenVal == 'send':
                        inst_line += 'print($v)'
                else: # assuming a variable
                    

                    if tokenVal.startswith("{") and tokenVal.endswith("}"):
                        tokenVal = tokenVal[1:-1]
                        # tokenVal = tokenVal.replace("{", "").replace("}", "")
                    else:
                        break

                    if arrVal(line, y+1)[1] == '=':
                        if line[y-1][1] == 'set':
                            if tokenVal in Vars:
                                # throw error
                                break
                            else:
                                if re.match(r'[.a-zA-Z0-9_]+', tokenVal):
                                    inst_line = inst_line.replace('$v', tokenVal)
                                else:
                                    # throw error
                                    break
                        else:
                            if tokenVal in Vars:
                                inst_line = 'Vars["'+ tokenVal + '"]'
                            else:
                                # throw error
                                break
                    else:
                        if tokenVal in Vars:
                            inst_line = inst_line.replace('$v', '"'+str(Vars[tokenVal])+'"')
            elif token[0] == 'expression':
                inst_line += tokenVal
            elif token[0] == 'number':
                inst_line += tokenVal
            elif token[0] == 'string':
                inst_line += '"' + tokenVal.replace('"', '') + '"'
        exec(inst_line)
    return lines


parse("example.sk")