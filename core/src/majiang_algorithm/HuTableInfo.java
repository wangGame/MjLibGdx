package majiang_algorithm;

public class HuTableInfo
{
	public byte needGui;
	public boolean jiang;
	public byte[] hupai = new byte[9];

	@Override
	public String toString()
	{
		String tmp = "";
		int index = 1;
		if (hupai == null)
		{
			tmp = "胡清";
		}
		else
		{
			for (byte i : hupai)
			{
				if (i > 0)
				{
					tmp += "胡" + (index);
				}
				index++;
			}
		}
		return tmp + " 将" + (jiang ? "1" : "0") + " 鬼" + needGui;
	}

}
