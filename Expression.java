import java.util.*;
public class Expression
{
	public final int OPERAND = 0;
	public final int OPERATOR = 1;
	public Node root;

	/**
	* creates a new expression with root r
	* @param r the root 
	*/
	public Expression(Node r)
	{
		root = r;
	}
	
	/**
	* conerts an infix expression string into an expression tree
	* @param in the infix string
	* @return an expression constructed from in.
	*/
	public static Expression infixToExp(String in)
	{
		String[] tokens = Convert.inToPost(Convert.stringToTokens(in));
		Stack<Node> st = new Stack<Node>();
		Node n = null;
		for(int i=0;i<tokens.length;i++)
		{
			switch(tokens[i])
			{
				case "+": 
					n = new Node("+",0);
					n.rchild = st.pop();
					n.lchild = st.pop();
					st.push(n);
					break;
				case "-": 
					n = new Node("-",0);
					n.rchild = st.pop();
					n.lchild = st.pop();
					st.push(n);
					break;
				case "*": 
					n = new Node("*",0);
					n.rchild = st.pop();
					n.lchild = st.pop();
					st.push(n);
					break;
				case "/": 
					n = new Node("/",0);
					n.rchild = st.pop();
					n.lchild = st.pop();
					st.push(n);
					break;
				case "^": 
					n = new Node("^",0);
					n.rchild = st.pop();
					n.lchild = st.pop();
					st.push(n);
					break;
				default:
					if(isNumber(tokens[i]))
						st.push(new Node(tokens[i],1));
					else	
						st.push(new Node(tokens[i],2));
					break;
			}
		}
		return new Expression(st.pop());
	}

	/**
	* the expression tree as a string
	* @return the expression tree as a string
	*/
	public String toString()
	{
		String ret = toString(root);
		return ret;
	}

	/**
	* the subtree at node n as a string
	* @param n a node in the expression tree
	* @return a string representation fo the subtree at n
	*/
	public String toString(Node n)
	{
		String ret = "";
		if(n.lchild != null)
			ret += toString(n.lchild);
		if(n.rchild != null)
			ret += toString(n.rchild);
		ret += n;
		return ret;
	}

	/**
	* tests if the given string is a number
	* @param in the string to be tested
	* @return true if in is a number; false otherwise
	*/
	public static boolean isNumber(String in) 
	{
		try
		{
			Double.parseDouble(in);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	/**
	* attempts to simplify the expression
	*/
	public void simplify()
	{
		root.resolve();
	}

	/**
	* computes the derivative of the expression
	* @return the derivative as an expression tree
	*/
	public Expression derivative()
	{
		return new Expression(root.differentiate());
	}
}
