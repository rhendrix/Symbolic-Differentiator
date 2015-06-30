import java.util.*;
public class Convert
{
    private static final int LEFT_ASSOC = 0;
    private static final int RIGHT_ASSOC = 1;
			
    private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();
    static
    {
		OPERATORS.put("+", new int[] {0, LEFT_ASSOC});
		OPERATORS.put("-", new int[] {0, LEFT_ASSOC});
		OPERATORS.put("*", new int[] {5, LEFT_ASSOC});
		OPERATORS.put("/", new int[] {5, LEFT_ASSOC});
		OPERATORS.put("%", new int[] {5, LEFT_ASSOC});
		OPERATORS.put("^", new int[] {10, LEFT_ASSOC});
    }

	/**
	* determines if a token is an operator
	* @param token the token
	* @return true if the token is an operator, false otherwise 
	*/
    private static boolean isOperator(String token)
    {
		return OPERATORS.containsKey(token);
    }

	/**
	* checks the associativty of an operator
	* @param token the operator
	* @param type the associativity
	* @return true if the associativity of the operator matches type
	*/
    private static boolean isAssociative(String token, int type)
    {
		if(!isOperator(token))
			throw new IllegalArgumentException("Invalid token: " + token);
		if(OPERATORS.get(token)[1] == type)
			return true;
		return false;
    }

	/**
	* compares the precedence of two operators
	* @param token1 the first operator
	* @param token2 the second operator
	* @return the differnece between the precedence of token1 and token2
	*/
    private static final int cmpPrecedence(String token1, String token2)
    {
		if(!isOperator(token1) || !isOperator(token2))
			throw new IllegalArgumentException("Invalid token: " + token1 + " " + token2);
		return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];
    }

	/**
	* converts a tokenized infix expression to a postfix expression using shunting yard algorithm
	* @param inputTokens tokenized infix expression
	* @return tokenized postfix expression
	*/
    public static String[] inToPost(String[] inputTokens)
    {
		ArrayList<String> out = new ArrayList<String>();
		Stack<String> stack = new Stack<String>();
		for(String token : inputTokens)
		{
			if(isOperator(token))
			{
				stack.push(token);
				while(!stack.empty() && isOperator(stack.peek()))
				{
					if((isAssociative(token, LEFT_ASSOC) && cmpPrecedence(token, stack.peek()) < 0) || (isAssociative(token, RIGHT_ASSOC) && cmpPrecedence(token, stack.peek()) < 0))
					{
						System.out.println("Yep");
						out.add(stack.pop());
						continue;
					}
					break;
				}
			}
			else if(token.equals("("))
			{
				stack.push(token);
			}
			else if(token.equals(")"))
			{
				while(!stack.empty() && !stack.peek().equals("("))
				{
					out.add(stack.pop());
				}
				stack.pop();
			}
			else
			{
				out.add(token);
			}
		}
		while(!stack.empty())
		{
			out.add(stack.pop());
		}
		String[] output = new String[out.size()];
		return out.toArray(output);
    }

	/**
	* converts an infix expression string to a tokenized postfix expression
	* @param input the infix string
	* @return a tokenized postfix exression
	*/
    public static String inToPost(String input)
    {
		String[] out = inToPost(stringToTokens(input));
		return tokensToString(out);
    }

	/**
	* tokenizes an infix expression
	* @param s infix string
	* @return a tokenized infix expression
	*/
	public static String[] stringToTokens(String s)
	{
		String noSpace = noSpace(s);
		ArrayList<String> tokens = new ArrayList<String>();
		String tok = "";
		String op = "";
		int i = 0;
		while(i<noSpace.length())
		{
			if(isOperator(op = Character.toString(noSpace.charAt(i))))
			{
				if(!tok.equals(""))
					tokens.add(tok);
				tok = "";
				tokens.add(op);
				i++;
			}
			else if(noSpace.charAt(i) == '(' || noSpace.charAt(i) == ')')
			{
				if(!tok.equals(""))
					tokens.add(tok);
				tok = "";
				tokens.add(Character.toString(noSpace.charAt(i)));
				i++;
			}
			else
			{
				tok = tok + noSpace.charAt(i);
				i++;
			}
		}
		if(!tok.equals(""))
			tokens.add(tok);
		String[] out = new String[tokens.size()];
		tokens.toArray(out);
		return out;
	}

	/**
	* untokenizes an infix expression
	* @param s tokenized infix expression
	* @return infix string
	*/
	public static String tokensToString(String[] s)
	{
		String out = "";
		for(int i=0;i<s.length;i++)
		{
			out += s[i];
		}
		return out;
	}
	
	/**
	* strips spaces from a string
	* @param s the string
	* @return string without spaces
	*/
	public static String noSpace(String s)
	{	
		String[] out = s.split(" ");
		String noSpace = "";
		for(int i=0;i<out.length;i++)
		{
			noSpace += out[i];
		}
		return noSpace;
	}
}
