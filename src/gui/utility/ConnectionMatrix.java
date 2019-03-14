package gui.utility;

import logic.Tilemap;
import logic.utility.Color;
import logic.utility.TileType;

public class ConnectionMatrix
{
	public static ConnectionType getConnectionType(int absolute_x, int absolute_y, Tilemap map)
	{
		//From complex to simple (JUNCTION to NONE)
		//The collection_index is also the type of connection (as it will be parsed to enum type)
		for (int collection_index = MATRICES.length-1; collection_index >= 0; collection_index--)
		{
			boolean[][][] collection = MATRICES[collection_index];
			//check against each Matrix collection
			for (int matrix_index = 0; matrix_index < collection.length; matrix_index++)
			{
				boolean[][] matrix = collection[matrix_index];
				//Check each specific matrix, using x and y as offset parameters
				boolean matrix_fits = applyMatrix(matrix, absolute_x, absolute_y, map);
				if (matrix_fits)
					return ConnectionType.values()[collection_index];
			}
		}
		
		return ConnectionType.NONE;
	}
	
	private static boolean applyMatrix(boolean[][] matrix, int absolute_x, int absolute_y, Tilemap map)
	{
		boolean is_wall = map.getTiles()[absolute_x][absolute_y].getType() == TileType.WALL;
		Color color = map.getTiles()[absolute_x][absolute_y].getColor();
		
		if (color == Color.NONE && !is_wall)
			return false;
		
		for (int x = -1; x <= 1; x++ )
		{
			for (int y = -1; y <= 1; y++)
			{
				int matrix_x  = x + 1, matrix_y = y + 1;
				int tilemap_x = x + absolute_x, tilemap_y = y + absolute_y;
				
				//Field is not required?
				if (!matrix[matrix_x][matrix_y])
					continue;
				
				//Check if the coords are out of bounds
				boolean invalid_coordinates = false;
				if (tilemap_x < 0 || tilemap_x >= map.getWidth())
					invalid_coordinates = true;
				else if(tilemap_y < 0 || tilemap_y >= map.getHeight())
					invalid_coordinates = true;
				
				//Field is required but the coordinates are invalid
				if (invalid_coordinates && matrix[matrix_x][matrix_y])
					return false;
				
				//Check if the current tile in the tilemap is of different color or wall
				//At this point we know that coord in the matrix is important!
				if (is_wall)
				{
					if (map.getTiles()[tilemap_x][tilemap_y].getType() != TileType.WALL)
						return false;
				}
				else
				{
					if (map.getTiles()[tilemap_x][tilemap_y].getColor() != color)
					{
						if (map.getEndPoint() != null)
						{
							if (map.getEndPoint().getX() != tilemap_x || map.getEndPoint().getY() != tilemap_y)
								return false;
						}
					}
				}
			}
		}
		
		return true;
	}
	
	public static final boolean[][][][] MATRICES = new boolean[][][][]
	{
		//None (1 variation)
		new boolean[][][]
		{
			new boolean[][]
			{
				new boolean[] { false, false, false },
				new boolean[] { false, true, false },
				new boolean[] { false, false, false }
			} 
		},
		//Vertical (2 variations)
		new boolean[][][] 
		{
			new boolean[][]
			{
				new boolean[] { false, false, false },
				new boolean[] { false, true, false },
				new boolean[] { false, true, false }
			},
			new boolean[][]
			{
				new boolean[] { false, true, false },
				new boolean[] { false, true, false },
				new boolean[] { false, false, false }
			},
		},
		//horizontal (2 variations)
		new boolean[][][] 
		{
			new boolean[][]
			{
				new boolean[] { false, false, false },
				new boolean[] { true, true, false },
				new boolean[] { false, false, false }
			},
			new boolean[][]
			{
				new boolean[] { false, false, false },
				new boolean[] { false, true, true },
				new boolean[] { false, false, false }
			} 
		},
		//Left_to_bottom (1 variation)
		new boolean[][][] 
		{
			new boolean[][]
			{
				new boolean[] { false, false, false },
				new boolean[] { true, true, false },
				new boolean[] { false, true, false }
			} 
		},
		//left_to_top (1 variation)
		new boolean[][][] 
		{
			new boolean[][]
			{
				new boolean[] { false, true, false },
				new boolean[] { true, true, false },
				new boolean[] { false, false, false }
			} 
		},
		//right_to_top (1 variation)
		new boolean[][][] 
		{
			new boolean[][]
			{
				new boolean[] { false, true, false },
				new boolean[] { false, true, true },
				new boolean[] { false, false, false }
			} 
		},
		//right_to_bottom (1 variation)
		new boolean[][][] 
		{
			new boolean[][]
			{
				new boolean[] { false, false, false },
				new boolean[] { false, true, true },
				new boolean[] { false, true, false }
			} 
		},
		//t_junction_top (1 variation)
		new boolean[][][] 
		{
			new boolean[][]
			{
				new boolean[] { false, true, false },
				new boolean[] { true, true, true },
				new boolean[] { false, false, false }
			} 
		},
		//t_junction_bottom (1 variation)
		new boolean[][][] 
		{
			new boolean[][]
			{
				new boolean[] { false, false, false },
				new boolean[] { true, true, true },
				new boolean[] { false, true, false }
			} 
		},
		//t_junction_left (1 variation)
		new boolean[][][] 
		{
			new boolean[][]
			{
				new boolean[] { false, true, false },
				new boolean[] { true, true, false },
				new boolean[] { false, true, false }
			} 
		},
		//t_junction_right (1 variation)
		new boolean[][][] 
		{
			new boolean[][]
			{
				new boolean[] { false, true, false },
				new boolean[] { false, true, true },
				new boolean[] { false, true, false }
			} 
		},
		//junction (1 variation)
		new boolean[][][] 
		{
			new boolean[][]
			{
				new boolean[] { false, true, false },
				new boolean[] { true, true, true },
				new boolean[] { false, true, false }
			} 
		}
	};
}
