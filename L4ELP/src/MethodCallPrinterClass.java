
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.github.javaparser.ASTHelper;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


//****************************************************************************
//This file will be used to create  method call from a given fragment of code
//****************************************************************************
public class MethodCallPrinterClass
{
	String method_call_names = "";
	boolean flag =  true;
    public static void main(String[] args) throws Exception
    {
        //FileInputStream in = new FileInputStream("F:\\Research\\git-repositories\\logging2\\logging2\\src\\test.java");

        
        String str = "class a {  " +
				"	public static void main () {BeanInfo info=Introspector.getBeanInfo(base.getClass()); " +
				"	PropertyDescriptor[] pds=info.getPropertyDescriptors();  " +
				" for (int i=0; i < pds.length; i++) { " +
			    "	pds[i].setValue(RESOLVABLE_AT_DESIGN_TIME,Boolean.TRUE);  " +
                "  pds[i].setValue(TYPE,pds[i].getPropertyType());  " +
                " }  " +
                "  return Arrays.asList((FeatureDescriptor[])pds).iterator();  " +
                "  }}" ;
    }
    
    public String visitor(String str)
    {

    	
    	method_call_names= "";
    	// convert String into InputStream
    	InputStream is = new ByteArrayInputStream(str.getBytes());

    	// read it with BufferedReader
    	BufferedReader br = new BufferedReader(new InputStreamReader(is));
    	util4_met utm= new util4_met();
          
        CompilationUnit cu = null;
        try
        {
            
				cu = JavaParser.parse(is);
			
        } catch (ParseException e) 
        {
        	   System.out.println(" exception in parse:=:"+ str );
        	
				//utm.interupt();
				e.printStackTrace();
		}
        
        finally
        {
              try 
                 {
			       	is.close();
			     
                 } catch (IOException e)
                 {
				 
				        e.printStackTrace();
			      }
        }
        new MethodVisitor().visit(cu, null);
  
    return method_call_names;
    }

    private  class MethodVisitor extends VoidVisitorAdapter
    {
        @Override
        public void visit(MethodCallExpr methodCall, Object arg)
        {
           // System.out.print("Method call: " + methodCall.getName() + "\n");
        	String temp2 = "";
        	temp2 = methodCall.getName();
        	
        	if(temp2.compareToIgnoreCase("sangeeta")==0)
        	{
        		//termination point is reached 
        		flag= false;
        	}
        
        	//=== This will be used make the flag false as it ==//
        	if(flag)
        	{ 
        		method_call_names =  methodCall.getName()+" "+ method_call_names; 
        	}
        	
            List<Expression> args = methodCall.getArgs();
            if (args != null)
                handleExpressions(args);
        }

        private void handleExpressions(List<Expression> expressions)
        {
        	//System.out.println("trace 2");
            for (Expression expr : expressions)
            {
                if (expr instanceof MethodCallExpr)
                    visit((MethodCallExpr) expr, null);
                else if (expr instanceof BinaryExpr)
                {
                    BinaryExpr binExpr = (BinaryExpr)expr;
                    handleExpressions(Arrays.asList(binExpr.getLeft(), binExpr.getRight()));
                }
            }
        }
    }
}