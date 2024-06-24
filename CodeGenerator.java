import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CodeGenerator
{
    public static void main(String[] args)
    {
        String fileName = args[0];
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            Map<String, StringBuilder> classContents = new HashMap<>();
            String line;
            String currentClass = null;
            while ((line = br.readLine()) != null)
            {
                String[] parts = line.split(" ");
                //classDiagram
                if(line.trim().startsWith("classDiagram"))
                {
                    continue ;
                }
                //class a{...} or class a
                else if (line.trim().startsWith("class"))
                {
                    if(line.contains("{")&& parts[parts.length-1].length() > 1)
                    {
                        currentClass = parts[parts.length-1].replaceAll("[^a-zA-Z]", "").trim();
                    }
                    else if(line.contains("{"))
                    {
                        currentClass = parts[parts.length-2].replaceAll("[^a-zA-Z]", "").trim();
                    }
                    else
                    {
                        currentClass = parts[parts.length-1].replaceAll("[^a-zA-Z]", "").trim();
                    }
                    classContents.putIfAbsent(currentClass, classBuilder.createClass(currentClass));
                }
                //a: ...
                else if (line.contains(":"))
                {
                    String name = getValue(line);
                    currentClass = name ;
                    if(currentClass != null)
                    {
                        classContents.get(currentClass).append(returnpp.pp(line));
                        if(line.contains("()"))
                        {
                            if(line.replaceAll("[^a-zA-Z]", "").endsWith("String") || line.replaceAll("[^a-zA-Z]", "").endsWith("int") || line.replaceAll("[^a-zA-Z]", "").endsWith("boolean") ||line.replaceAll("[^a-zA-Z]", "").endsWith("void")||line.replaceAll("[^a-zA-Z]", "").endsWith("double") )
                            {
                                classContents.get(currentClass).append(" "+parts[parts.length-1].replaceAll("[^a-zA-Z]", "")+retrunLine.lines(line, parts.length-1)+returnName2.name2(line));
                            }
                            else
                            {
                                classContents.get(currentClass).append(" void "+parts[parts.length-1].replaceAll("[^a-zA-Z]", "")+retrunLine.lines(line, parts.length-1)+returnName2.name2(line));
                            }
                        }
                        else if(line.contains("("))
                        {
                            if(line.replaceAll("[^a-zA-Z]", "").endsWith("String") ||line.replaceAll("[^a-zA-Z]", "").endsWith("int") ||line.replaceAll("[^a-zA-Z]", "").endsWith("boolean") ||line.replaceAll("[^a-zA-Z]", "").endsWith("void") ||line.replaceAll("[^a-zA-Z]", "").endsWith("double"))
                            {
                                classContents.get(currentClass).append(" "+parts[parts.length-1]+retrunLine.lines(line, parts.length-1)+returnName2.name2(line));
                            }
                            else
                            {
                                classContents.get(currentClass).append(" void "+parts[parts.length-1]+retrunLine.lines(line, parts.length-1)+returnName2.name2(line));
                            }
                        }

                        else
                        {
                            classContents.get(currentClass).append(retrunLine.lines(line, parts.length)+";\n");
                        }
                    }
                }
                //a{...}
                else 
                {
                    if(currentClass != null && returnpp.pp(line) != null)
                    {
                        classContents.get(currentClass).append(returnpp.pp(line));
                        if(line.contains("()"))
                        {
                            if(line.replaceAll("[^a-zA-Z]", "").endsWith("String") || line.replaceAll("[^a-zA-Z]", "").endsWith("int") || line.replaceAll("[^a-zA-Z]", "").endsWith("boolean") ||line.replaceAll("[^a-zA-Z]", "").endsWith("void") ||line.replaceAll("[^a-zA-Z]", "").endsWith("double"))
                            {
                                classContents.get(currentClass).append(" "+parts[parts.length-1]+retrunLine.lines(line, parts.length-1)+returnName2.name2(line));
                            }
                            else
                            {
                                classContents.get(currentClass).append(" void "+parts[parts.length-1]+retrunLine.lines(line, parts.length-1)+returnName2.name2(line));
                            }
                        }
                        else if(line.contains("("))
                        {
                            if(line.replaceAll("[^a-zA-Z]", "").endsWith("String") || line.replaceAll("[^a-zA-Z]", "").endsWith("int") || line.replaceAll("[^a-zA-Z]", "").endsWith("boolean") ||line.replaceAll("[^a-zA-Z]", "").endsWith("void") ||line.replaceAll("[^a-zA-Z]", "").endsWith("double"))
                            {
                                classContents.get(currentClass).append(" "+parts[parts.length-1]+retrunLine.lines(line, parts.length-1)+returnName2.name2(line));
                            }
                            else
                            {
                                classContents.get(currentClass).append(" void "+parts[parts.length-1]+retrunLine.lines(line, parts.length-1)+returnName2.name2(line));
                            }
                        }
                        else
                        {
                            classContents.get(currentClass).append(retrunLine.lines(line, parts.length)+";\n");
                        }
                    }
                }
            }
            for (Map.Entry<String, StringBuilder> entry : classContents.entrySet())
            {
                String className = entry.getKey();
                StringBuilder classContent = entry.getValue();
                classContent.append("}\n");
                writeToFile(className + ".java", classContent.toString()); 
            }

            br.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private static String getValue(String input)
    {
        String[] parts = input.split(":");
        if(parts.length>0)
        {
            return parts[0].trim();
        }
        else
        {
            return input.trim();
        }
    }
    

    private static void writeToFile(String filename, String content)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            bw.write(content);
            bw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

class classBuilder
{
    public static StringBuilder createClass(String className)
    {
        return new StringBuilder("public class "+className+" {\n");
    }
}

class returnpp
{
    public static StringBuilder pp(String line)
    {
        if(line.contains("+"))
        {
            return new StringBuilder("    public");
        }
        else if(line.contains("-"))
        {
            return new StringBuilder("    private");
        }
        return null;
    }
}

class retrunLine
{
    public static StringBuilder lines(String line ,int j)
    {
        StringBuilder newLine = new StringBuilder();
        String[] parts = line.split(" ");
        for(int i = 0 ; i< parts.length ; i++)
        {
            //System.out.print(parts[i]);
            if((parts[i].contains("+")|| parts[i].contains("-"))&&parts[i].contains(":"))
            {
                for(int k = i ; k < j ; k++)
                {
                    if(parts[k].isEmpty())
                    {
                        continue;
                    }
                    else
                    {
                        int colonIndex = parts[k].indexOf(":");
                        if(colonIndex != -1)
                        {
                            newLine.append(" "+parts[k].substring(colonIndex+1).trim().replace("+", "").replace("-", "").replace(":", ""));
                        }
                        else
                        {
                            newLine.append(" "+parts[k].substring(colonIndex+1).trim().replace("+", "").replace("-", "").replace(":", ""));
                        }

                    }
                }
                break;
            }
            else if(parts[i].contains("+")|| parts[i].contains("-"))
            {
                for(int k = i ; k < j ; k++)
                {
                    if(parts[k].isEmpty())
                    {
                        continue;
                    }
                    else
                    {
                        newLine.append(" "+parts[k].trim().replaceAll("[\\+\\-]", ""));
                    }
                }
                break;
            }
        }
        return newLine ;
    }
    
}

class returnName2
{
    public static StringBuilder name2(String line)
    {
        String[] parts = line.split(" ");
        for(int i = 0 ; i <parts.length ; i++)
        {
            while(parts[i].contains("+") || parts[i].contains("-"))
            {
                //System.out.println(parts[i+1]);
                if(checkset(parts[i].replaceAll("[\\+\\-]", "")))
                {
                    StringBuilder result = lower(parts[i]);
                    if(line.replaceAll("[^a-zA-Z]", "").endsWith("String") || line.replaceAll("[^a-zA-Z]", "").endsWith("void")||line.replaceAll("[^a-zA-Z]", "").endsWith("int")||line.replaceAll("[^a-zA-Z]", "").endsWith("boolean"))
                    {
                        String name = parts[parts.length-2].replaceAll("[^a-zA-Z]", "");
                        return new StringBuilder(" {\n        this."+result+" = "+result+";\n    }\n");
                    }
                    else
                    {
                        String name = parts[parts.length-1].replaceAll("[^a-zA-Z]", "");
                        return new StringBuilder(" {\n        this."+result+" = "+result+";\n    }\n");
                    }
                    
                }
                else if(checkget(parts[i].replace("+", "").replace("-", "").replace(":", "")))
                {
                    StringBuilder result = lower(parts[i]);
                    //String name = parts[parts.length-3].replaceAll("[^a-zA-Z]", "");
                    return new StringBuilder(" {\n        return "+result+";\n    }\n");
                }
                else
                {
                    if(line.replaceAll("[^a-zA-Z]", "").endsWith("String"))
                    {
                        return new StringBuilder(" {return \"\";}\n");
                    }  
                    else if(line.replaceAll("[^a-zA-Z]", "").endsWith("void"))
                    {
                        return new StringBuilder(" {;}\n");
                    }
                    else if(line.replaceAll("[^a-zA-Z]", "").endsWith("int"))
                    {
                         return new StringBuilder(" {return 0;}\n");
                    }
                    else if(line.replaceAll("[^a-zA-Z]", "").endsWith("boolean"))
                    {
                        return new StringBuilder(" {return false;}\n");
                    }
                    else
                    {
                        return new StringBuilder(" {;}\n");
                    }
                }
            }
        }
        return null;
    }
    public static boolean checkget(String str)
    {
        if(str.length()>=3)
        {
            String firstVhars = str.substring(0, 3);
            return firstVhars.equals("get") ;
        }
        return false ;
    }
    public static boolean checkset(String str)
    {
        if(str.length()>=3)
        {
            String firstVhars = str.substring(0, 3);
            return firstVhars.equals("set");
        }
        return false ;
    }
    public static StringBuilder lower(String line)
    {
        String input = line;
        char[] chars = input.toCharArray();
        char uppercaseChar = '\0';
        boolean foundUppercase = false;
        for(int i = 0 ; i < chars.length ; i++)
        {
            if(Character.isUpperCase(chars[i]))
            {
                if( ! foundUppercase)
                {
                    uppercaseChar = chars[i];
                    foundUppercase = true ;
                }  
                else
                {
                    //System.out.println(" find more than 1 upperchar");
                    break ;
                }
            }
        }
        StringBuilder result = new StringBuilder() ;
        if(foundUppercase)
        {
            boolean convertToUpper = false ;
            for(int i = 0 ; i < input.length() ; i++)
            {
                char currentChar = input.charAt(i);
                if(currentChar == '(')
                {
                    break;
                }
                else
                {
                    if(convertToUpper)
                    {
                        result.append(currentChar);
                        //result.append(Character.toLowerCase(currentChar));
                    }
                    else if(currentChar == uppercaseChar)
                    {
                        
                        result.append(Character.toLowerCase(currentChar));
                        convertToUpper = true ;
                    }
                }      
            }
        }
        return new StringBuilder(result);
    }
}
