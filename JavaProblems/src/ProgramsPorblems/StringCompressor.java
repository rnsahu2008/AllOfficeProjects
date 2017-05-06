package ProgramsPorblems;

class StringCompressor 
{ 
       public static void main(String [] args) throws Exception 
       {
              StringCompressor ts = new StringCompressor();
              
              System.out.println(ts.compress("aabbhjhhhhhhjj"));
       }
       

       public String compress(String str)
       {
              StringBuffer result = new StringBuffer();
              char[] character = str.toCharArray();
              
              int count =1;
              for(int i=1 ; i<str.length();i++)
              {
                     if(character[i-1]==character[i])
                     {
                           count=count+1;
                           if(i==str.length()-1)
                           {
                                  result.append(Character.toString(character[i]));
                                  if(!(count==1))
                                  {
                                         result.append(count);
                                  }
                           }
                     }
                     else
                     {
                           result.append(Character.toString(character[i-1]));
                           if(!(count==1))
                           {
                                  result.append(count);
                           }
                           count=1;
                           if(i==str.length()-1)
                           {
                                  result.append(Character.toString(character[i]));
                           }
                     }
                     
              }
              return result.toString();
       }
}
