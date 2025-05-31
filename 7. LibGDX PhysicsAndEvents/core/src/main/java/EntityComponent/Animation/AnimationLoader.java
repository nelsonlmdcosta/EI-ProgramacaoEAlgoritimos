package EntityComponent.Animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;

// Splitting https://libgdx.com/wiki/graphics/2d/2d-animation
public class AnimationLoader
{
    // TODO: This Isn't Really Efficient As A Single One Does The Trick But At This Point I Just Want This Done! :p
    public static boolean LoadKaduki8WaySprite(Dictionary<Vector2, List<TextureRegion>> Output, String AssetPath4Way, String AssetPath4WayDiagonal)
    {
        Texture FourWayTexture = new Texture(AssetPath4Way);
        Texture FourWayTextureDiagonal = new Texture(AssetPath4WayDiagonal);

        TextureRegion[][] FourWayTextureSplit = TextureRegion.split(FourWayTexture, FourWayTexture.getWidth() / 3, FourWayTexture.getHeight() / 4);
        TextureRegion[][] FourWayTextureDiagonalSplit = TextureRegion.split(FourWayTextureDiagonal, FourWayTexture.getWidth() / 3, FourWayTexture.getHeight() / 4);

        // 4 Cardinal Direction
        Output.put(new Vector2(  0,  1), Arrays.asList( FourWayTextureSplit[3])); // Up
        Output.put(new Vector2(  1,  0), Arrays.asList( FourWayTextureSplit[2])); // Right
        Output.put(new Vector2(  0, -1), Arrays.asList( FourWayTextureSplit[0])); // Down
        Output.put(new Vector2( -1,  0), Arrays.asList( FourWayTextureSplit[1])); // Left

        // 4 Intermediate Directions
        Output.put(new Vector2(  1,  1), Arrays.asList( FourWayTextureDiagonalSplit[3])); // Top Right
        Output.put(new Vector2(  1, -1), Arrays.asList( FourWayTextureDiagonalSplit[2])); // Bottom Right
        Output.put(new Vector2( -1, -1), Arrays.asList( FourWayTextureDiagonalSplit[0])); // Bottom Left
        Output.put(new Vector2( -1,  1), Arrays.asList( FourWayTextureDiagonalSplit[1])); // Top Left

        return true;
    }
}
