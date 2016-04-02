

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//Link of this code:  http://stackoverflow.com/questions/1657066/java-regular-expression-finding-comments-in-code
public class Remove_Comments {



 List<Match> commentMatches = new ArrayList<Match>();

//This function removes the comments from given string
public String remove_comments(String text)
{
	
	 Pattern commentsPattern = Pattern.compile("(//.*?$)|(/\\*.*?\\*/)", Pattern.MULTILINE | Pattern.DOTALL);
	 Pattern stringsPattern = Pattern.compile("(\".*?(?<!\\\\)\")");
	 
	 Matcher commentsMatcher = commentsPattern.matcher(text);
     while (commentsMatcher.find()) 
     {
	        Match match = new Match();
	        match.start = commentsMatcher.start();
	        match.text = commentsMatcher.group();
	        commentMatches.add(match);
	 }
     
   
     
     List<Match> commentsToRemove = new ArrayList<Match>();

     Matcher stringsMatcher = stringsPattern.matcher(text);
     while (stringsMatcher.find()) {
         for (Match comment : commentMatches) {
             if (comment.start > stringsMatcher.start() && comment.start < stringsMatcher.end())
                 commentsToRemove.add(comment);
         }
     }
     
     
     for (Match comment : commentsToRemove)
         {commentMatches.remove(comment);
         
         }

     for (Match comment : commentMatches)
         {
    	   text = text.replace(comment.text, " ");
          
         }
    
     return text;

}

 class Match 
{
    int start;
    String text;
}
//
}