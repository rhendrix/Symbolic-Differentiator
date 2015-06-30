public class Node
{
	public String value;
	public int type, ary;
	public Node parent, lchild, rchild;

	/**
	* creates a new node with value v, type t, and ary 2.
	* @param v the value of the node
	* @param t the type of the node
	*/
	public Node(String v, int t)
	{
		value = v;
		type = t;
		ary = 2;
	}

	/**
	* creates a new node with value v, type t, and ary 2.
	* @param v the value of the node
	* @param t the type of the node
	* @param a whether the node is unary or binary
	*/
	public Node(String v, int t, int a)
	{
		value = v;
		type = t;
		ary = a;
	}

	/**
	* finds the leftmost node in a node''s subtree
	* @return the leftmost node
	*/
	public Node leftmost()
	{
		Node n = this;
		while(n.lchild != null)
		{
			n = n.lchild;
		}
		return n;
	}

	/**
	* attempts to resolve basic simplifications (0*x=0, 1*x=1, etc.)
	*/
	public void resolve()
	{
		if(lchild != null)
			lchild.resolve();
		if(rchild != null)
			rchild.resolve();
		if(type == 0 && number())
		{
			eval();
			return;
		}
		if(type == 0 && zero()!=0)
		{
			if(value.equals("+"))
			{
				if(zero()==1)
				{
					value = lchild.value;
					type = lchild.type;
					Node tl = lchild.lchild;
					Node tr = lchild.rchild;
					lchild = tl;
					rchild = tr;
					return;
				}
				else if(zero()==-1)
				{
					value = rchild.value;
					type = rchild.type;
					Node tl = rchild.lchild;
					Node tr = rchild.rchild;
					lchild = tl;
					rchild = tr;
					return;
				}
			}
			if(value.equals("*"))
			{
				value = "0";
				type = 1;
				lchild = null;
				rchild = null;
				return;
			}
			return;
		}
		if(type==0 && value.equals("*") && one()!=0)
		{
			if(one()==1)
			{
				value = lchild.value;
				type = lchild.type;
				Node tl = lchild.lchild;
				Node tr = lchild.rchild;
				lchild = tl;
				rchild = tr;
				return;
			}
			else if(one()==-1)
			{
				value = rchild.value;
				type = rchild.type;
				Node tl = rchild.lchild;
				Node tr = rchild.rchild;
				lchild = tl;
				rchild = tr;
				return;
			}
			return;
		}
	}

	/**
	* determines if the node should be a numebr (not a variable)
	* @return true if it should be a number, false otherwise
	*/
	public boolean number()
	{
		if(ary == 2)
			return lchild.type == 1 && rchild.type == 1;
		else if(ary == 1)
			return lchild.type == 1;
		return false;
	}

	/**
	* 
	*/
	public int zero()
	{
		if(lchild == null || rchild == null)
			return 0;
		if(lchild.type==1 && round(lchild.toDouble()) == 0)
			return -1;
		else if(rchild.type==1 && round(rchild.toDouble()) == 0)
			return 1;
		return 0;
	}

	/**
	*
	*/
	public int one()
	{
		if(lchild == null || rchild == null)
			return 0;
		if(lchild.type==1 && round(lchild.toDouble()) == 1)
			return -1;
		else if(rchild.type==1 && round(rchild.toDouble()) == 1)
			return 1;
		return 0;
	}

	/**
	*
	*/
	public static int round(double d)
	{
		if(0.0000001>d && -0.0000001<d)
			return 0;
		else if(1.0000001>d && 0.9999999<d)
			return 1;
		else
			return -49;
	}
	public double toDouble()
	{
		return Double.parseDouble(value);
	}
	public void eval()
	{
		double left = lchild.toDouble();
		double right = rchild.toDouble();
		switch(value)
		{
			case "+":
				value = "" + (left+right);
				break;
			case "-":
				value = "" + (left-right);
				break;
			case "*":
				value = "" + (left*right);
				break;
			case "/":
				value = "" + (left/right);
				break;
			case "^":
				value = "" + Math.pow(left,right);
				break;
		}
		type = 1;
		lchild = null;
		rchild = null;
	}
	public Node copy()
	{
		Node n = new Node(value,type,ary);
		if(lchild != null)
			n.lchild = lchild.copy();
		if(rchild != null)
			n.rchild = rchild.copy();
		return n;
	}
	public Node differentiate()
	{
		Node n, nl, nr;
		switch(type)
		{
			case 0:
				switch(value)
				{
					case "^":
						n = new Node("*",0);
						n.lchild = new Node("*",0);
						n.lchild.rchild = new Node(""+rchild.toDouble(),1);
						n.lchild.lchild = copy();
						n.rchild = lchild.differentiate();
						n.lchild.lchild.rchild = new Node(""+(rchild.toDouble()-1),1);
						return n;
					case "+":
						n = new Node("+",0);
						n.lchild = lchild.differentiate();
						n.rchild = rchild.differentiate();
						return n;
					case "-":
						n = new Node("-",0);
						n.lchild = lchild.differentiate();
						n.rchild = rchild.differentiate();
						return n;
					case "*":
						n = new Node("+",0);
						n.lchild = new Node("*",0);
						n.rchild = new Node("*",0);
						n.lchild.lchild = lchild.differentiate();
						n.lchild.rchild = rchild.copy();
						n.rchild.lchild = rchild.differentiate();
						n.rchild.rchild = lchild.copy();
						return n;
					case "/":
						n = new Node("/",0);
						nl = new Node("-",0);
						nl.lchild = new Node("*",0);
						nl.rchild = new Node("*",0);
						nl.lchild.lchild = lchild.differentiate();
						nl.lchild.rchild = rchild.copy();
						nl.rchild.lchild = rchild.differentiate();
						nl.rchild.rchild = lchild.copy();
						nr = new Node("^",0);
						nr.lchild = rchild.copy();
						nr.rchild = new Node("2",1);
						n.lchild = nl; 
						n.rchild = nr;
						return n;
				}
				break;
			case 1:
				return new Node("0",1);
			case 2:
				return new Node("1",1);
		}
		return null;
	}
	public String toString()
	{
		return value;
	}
} 
