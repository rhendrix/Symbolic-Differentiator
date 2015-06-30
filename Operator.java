public enum Operator
{
	ADD("+",2),
	NEG("-",2),
	MULT("*",2),
	DIV("/",2),
	EXP("^",2);
	private final String note;
	private final int ary;
	Operator(String s, int a)
	{
		note = s;
		ary = a;
	}
	String note()
	{
		return note;
	}
	int ary()
	{
		return ary;
	}
	public String toString()
	{
		return note;
	}
}
