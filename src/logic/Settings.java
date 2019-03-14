package logic;

public class Settings
{
	/*
	 * The map to be played
	 */
	private Tilemap map;

	/*
	 * The delay before hiding the map in milliseconds
	 */
	private int delayBeforeHidingMap;

	/*
	 * The scaling factore of the illumination. value from 0.0 to 5.0, while 0.0 is
	 * completely hidden
	 */
	private float illuminationScale;

	/*
	 * The opacity of the mask layed over the map when it is hidden. value from 0.0
	 * to 1.0, while 0.0 is disabling the mask.
	 */
	private float shadowOpacity;
	
	public Settings()
	{
		applyDefault();
	}

	public void setShadowOpacity(float opacity)
	{
		// Validate opacity
		opacity = (opacity > 1.0f) ? 1.0f : opacity;
		opacity = (opacity < 0.0f) ? 0.0f : opacity;

		this.shadowOpacity = opacity;
	}

	public void setIlluminationScale(float scale)
	{
		// Validate opacity
		scale = (scale > 5.0f) ? 5.0f : scale;
		scale = (scale < 0.0f) ? 0.0f : scale;

		this.illuminationScale = scale;
	}
	
	public boolean isValid()
	{
		if (map == null)
			return false;
		
		if (!map.isValid())
			return false;
		
		return true;
	}
	
	public void applyDefault()
	{
		delayBeforeHidingMap = Defaults.DELAY_BEFORE_HIDING_MAP;
		illuminationScale = Defaults.ILLUMINATION_SCALE;
		shadowOpacity = Defaults.SHADOW_OPACITY;
	}

	public float getShadowOpacity()
	{
		return shadowOpacity;
	}
	
	public float getIlluminationScale()
	{
		return illuminationScale;
	}
	
	public int getDelayBeforeHidingMap()
	{
		return delayBeforeHidingMap;
	}
}
