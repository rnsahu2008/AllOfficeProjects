package ProgramsPorblems;

public class TraverseSpiralOrder
{	
	public void spiralprint(int m, int n, int[][] arr) {
		int k = 0, l = 0;
		int i;
	while(k<m && l<n)
	{
		
	for(i=l ;i<n;i++)
	{
		System.out.print(arr[k][i]+";");
		
	}
		k++;
		
		for(i=k ;i<m;i++)
		{
			System.out.print(arr[i][n-1]+";");
			
		}	
		n--;
		
		if(k<m)
		{
			//System.out.println(l+"t");
		for(i=n-1 ;i>=l;i--)
		{
			System.out.print(arr[m-1][i]+";");
			
		}	
		m--;
		}
		if(l<n)
		{
		for(i=m-1 ;i>=k;i--)
		{
			System.out.print(arr[i][l]+";");
			
		}	
		//n--;
		l++;
		}
	}		
	}
		
	//print diagonal
	public void digonal(int[][] arr,int m,int n)
	{
	for(int i=0;i<m;i++)
	{
		int j=i;
		System.out.println(arr[i][j]);
		
		
	}
		
	}
	public static void main(String[] args) 
	{
		int[][] p={{2,3,4,5,6},
				   {3,4,8,6,9},
				   {3,4,5,8,4},
				   {1,4,3,2,8},
				   {9,7,5,2,1}};
		TraverseSpiralOrder sd = new TraverseSpiralOrder();
		sd.spiralprint(4, 5, p);
		sd.digonal(p, 5, 5);

	}

}
