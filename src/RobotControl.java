import javax.swing.JOptionPane;

class RobotControl
{
	private Robot r;
	public static StringBuilder sb;

	public RobotControl(Robot r)
	{
		this.r = r;
	}

	public void control(int barHeights[], int blockHeights[])
	{
		run(barHeights, blockHeights);
	}

	public void run(int barHeights[], int blockHeights[])
	{
		int h = 2, w = 1, d = 0; // declaring h,w and d.

		int a = 0, b = 0; // used in loops

		final int MAX_W = 10; // declaring source position

		int bn = blockHeights.length; // short for block number

		int[] block = new int[11];

		while (b != 11)
		{
			block[b] = b;
			b++;
		}
		// Declaring an array for blocks

		for (int i = 0; i < bn; i++)
		{
			a += blockHeights[i];
		}

		int sourceh = a; // source height, sum of all blocks

		while (h != sourceh + 1)
		{
			r.up();
			h++;
		}

		for (int p = 1; p < (bn + 1); p++)
		{

			int NoOfBlock[] = new int[4];
			NoOfBlock = checkblocks(blockHeights, bn, p);

			w = change_w(w, MAX_W);

			// lowering d for pick
			if (p != 1)
			{

				int d_low = h - (sourceh + 1);

				d = change_d(d, d_low);
			}
			// lowering d for pick
			r.pick();

			int x = h - (d + blockHeights[bn - p] + 1); // x=sum of remaining blocks

			if (blockHeights[bn - p] == 3)
			{
				int BarsForBlock3BeforeDrop[] = new int[8];
				BarsForBlock3BeforeDrop = checkbarsforblock3(barHeights, blockHeights, bn, p);

				// raise d after pick

				if (d != 0)
				{
					int y = 7;
					if (x < 7)
					{
						while (x < y)
						{

							if (BarsForBlock3BeforeDrop[y] != 0)
							{
								int Raise_d = y - x;

								int newd9 = d - Raise_d;

								d = change_d(d, newd9);
								break;
							}
							y--;
						}
					}
				}

				// raise d after pick

				// contract w

				w = change_w(w, (NoOfBlock[3] + 3));

				// contract w

				// lower d to drop

				int d_Drop = (h - (barHeights[w - 3] + 4));

				d = change_d(d, d_Drop);

				// lower d to drop

				r.drop();
				if (p != bn)
				{
					// raise d after drop

					int BarsForBlock3AfterDrop[] = new int[8];
					BarsForBlock3AfterDrop = checkbarsforblock3(barHeights, blockHeights, bn, (p + 1));

					int x3 = (barHeights[w - 3] + 3);
					int y3 = 7;

					if (x3 < x)
					{
						d = change_d(d, (d - (x - x3)));
						x3 += x - x3;
					}

					if (x3 < 7)
					{
						while (x3 < y3)
						{

							if (BarsForBlock3AfterDrop[y3] != 0)
							{
								int Raise_d = y3 - x3;

								int newd3 = d - Raise_d;

								d = change_d(d, newd3);

							}
							y3--;
						}
					}
				}
				// raise d after drop
			} else
			{
				int BSize = blockHeights[bn - p];
				int BarsForBlocks12[] = new int[8];
				BarsForBlocks12 = checkbarsforblocks12(barHeights);

				int BarsForBlocks12_afterPick[] = new int[8];
				BarsForBlocks12_afterPick = checkbarsforblocks12_afterPick(barHeights, NoOfBlock[3]);

				// raise d after pick

				int x123 = 0; // x123 is used to calculate the maximum bar length + block 3

				int a12 = 7;

				if (NoOfBlock[3] != 0)
				{
					while (a12 > 0)
					{
						if (BarsForBlocks12_afterPick[a12] != 0)
						{
							x123 = a12 + 3;
							a12 = 0;
						}
						a12--;
					}
				}

				if (x123 >= x)
				{
					d = change_d(d, h - (x123 + blockHeights[bn - p] + 1));
				}

				if (x123 < x && x123 <= 7)
				{
					if (d != 0)
					{
						int y = 7;
						if (x < 7)
						{
							while (x < y)
							{
								if (BarsForBlocks12[y] != 0)
								{
									int Raise_d = y - x;

									int newd9 = d - Raise_d;

									d = change_d(d, newd9);
									break;
								}
								y--;
							}
						}
					}
				}

				if (x123 == 0 && x == 0)
				{
					int e12 = 7, f12 = 0;

					while (e12 > 0)
					{
						if (BarsForBlocks12[e12] != 0)
						{
							f12 = e12;
							e12 = 0;
						}
						e12--;
					}

					d = change_d(d, (h - (f12 + 1 + blockHeights[bn - p])));
				}
				// raise d after pick

				// contract w
				w = change_w(w, blockHeights[bn - p]);
				// contract w

				// lower d to drop

				int x12 = ((NoOfBlock[BSize] + 1) * BSize);

				d = change_d(d, (h - (x12 + 1)));

				// lower d to drop
				r.drop();

				if (p != bn)
				{
					// raise d after drop

					if (x123 > x)
					{
						d = change_d(d, (h - (x123 + 1)));
					}

					else if (x12 < x)
					{
						d = change_d(d, (h - (x + 1)));
						x12 += x - x12;
					}

					int q12 = 7, w12 = 0;

					while (q12 > 0)
					{
						if (BarsForBlocks12[q12] != 0)
						{
							w12 = q12 + 3;
							q12 = 0;
						}
						q12--;
					}
					if (x12 < w12)
					{
						d = change_d(d, (h - (w12 + 1)));
					}

					if (x123 > sourceh && x123 > (h - (d + 1)))
					{
						int new_d = h - (x123 + 1);
						d = change_d(d, new_d);
					}
					// raise d after drop
				}
			}
			sourceh -= blockHeights[bn - p];
		}
	}

	// changes w to desired number
	public int change_w(int w, int new_w)// done
	{
		if (w < new_w)
		{
			while (w != new_w)
			{
				r.extend();
				w++;
			}
		}

		else if (w > new_w)
		{
			while (w != new_w)
			{
				r.contract();
				w--;
			}
		}
		return w;
	}

	// changes d to desired number
	public int change_d(int d, int new_d)// done
	{
		while (d != new_d)
		{
			if (d < new_d)
			{
				r.lower();
				d++;
			}

			else if (d > new_d)
			{
				r.raise();
				d--;
			}
		}
		return d;
	}

	// checks for bars from the ending to the dropping bar
	public int[] checkbarsforblock3(int barHeights[], int blockHeights[], int bn, int pblock)// done
	{

		int[] check = new int[8];
		int a = 0;
		while (a != 7)
		{
			check[a] = 0;
			a++;
		}

		int block[] = new int[4];
		block = checkblocks(blockHeights, bn, pblock);

		int barss = 5;
		while (barss != (block[3] - 1))
		{
			int j = 2;
			while (j != 8)
			{
				if (barHeights[barss] == j)
				{
					check[j]++;
				}
				j++;
			}
			barss--;
		}

		return check;
	}

	// checks all bars
	public int[] checkbarsforblocks12(int barHeights[])// done
	{
		int[] check = new int[8];
		int a = 0;
		while (a != 7)
		{
			check[a] = 0;
			a++;
		}

		int barss = 6;
		while (barss != 0)
		{
			int j = 2;
			while (j != 8)
			{
				if (barHeights[barss - 1] == j)
				{
					check[j]++;
				}
				j++;
			}
			barss--;
		}
		return check;
	}

	// checks only the bars with block 3s on it
	public int[] checkbarsforblocks12_afterPick(int barHeights[], int block3s)// done
	{
		int[] check = new int[8];
		int a = 0;
		while (a != 7)
		{
			check[a] = 0;
			a++;
		}
		int barss = 1;
		while (barss != (block3s + 1))
		{
			int j = 1;
			while (j != 8)
			{
				if (barHeights[barss - 1] == j)
				{
					check[j]++;
				}
				j++;
			}
			barss++;
		}
		return check;
	}

	// checks all the previous blocks
	public int[] checkblocks(int blockHeights[], int bn, int pblock)// done
	{

		int[] block = new int[4];
		int a = 0;
		while (a != 4)
		{
			block[a] = 0;
			a++;
		}

		int x = pblock - 1;

		while (x != 0)
		{
			for (int i = 1; i < 4; i++)
			{
				if (blockHeights[bn - x] == i)
				{
					block[i]++;
				}
			}
			x--;
		}
		return block;
	}
}
