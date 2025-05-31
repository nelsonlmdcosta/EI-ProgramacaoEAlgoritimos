package EntityComponent.Animation;

import EntityComponent.AEntityComponent;
import EntityComponent.IUpdateableComponent;
import EntityComponent.Rendering.SpriteRendererComponent;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

// Example of creating an animation component, though we could technically just use the "2D Animation" in Libgdx, but if we dont do it how do we learn! Oh the humanity!
public class CharacterSpriteAnimator extends AEntityComponent implements IUpdateableComponent
{
    // TODO: Use Texture Regions
    private Dictionary<Vector2, List<TextureRegion>> SpriteRegions = new Hashtable<>();

    private SpriteRendererComponent SpriteRenderer = null;

    // This is a static number for now, but we should be able to change this depending on how fast we need to go from the input
    private float TimeBetweenTextureUpdates = 0.25f;
    private float Timer = 0.0f;
    private int AnimationOffset = 0;
    private int AnimationOffsetModifier = 1;

    private IAnimationMovementParameter AnimationMovementParameter = null;

    // Down And Idle By Default
    private Vector2 IdleDirection = new Vector2(0, -1);
    private Vector2 PreviousDirection = new Vector2();

    public void Start()
    {
        SpriteRenderer = Entity.GetFirstComponentOfType(SpriteRendererComponent.class);

        AnimationMovementParameter = Entity.GetFirstComponentWithInterface(IAnimationMovementParameter.class);

        // Loads a bunch of sprite regions could be more efficient but its easier this way for now :p
        AnimationLoader.LoadKaduki8WaySprite(SpriteRegions, "Images/Characters/Hero2/Actor7.png", "Images/Characters/Hero2/Actor7_5.png");
    }

    @Override
    public void Update(float DeltaTime)
    {
        // Keep Track Of The Movement Direction And Possible Animation Override
        Vector2 CurrentDirection = AnimationMovementParameter.MovementDirection();
        boolean OverrideAnimationSection = false;

        Timer += DeltaTime;


        // Override For When We Need To Be Idle
        // This means we're stopped so let's use the last cached direction and force the section to 1 which is the idle one (for Kaduki, so you'd need this to be some sort of param or seperate states.
        if(CurrentDirection.len() == 0.0f)
        {
            if(PreviousDirection.len() != 0.0f)
            {
                // Cache It Over Frames
                IdleDirection.set(PreviousDirection);
            }

            // Overload The Value For Now
            CurrentDirection = IdleDirection;
            OverrideAnimationSection = true;

            Timer = 0.0f;
            AnimationOffset = 0;
        }

        // TODO: Check for nulls! Naughty naughty! :p
        List<TextureRegion> SpriteRegion = SpriteRegions.get(CurrentDirection);

        // Check To See If It's Time To Switch Animations
        if(Timer > TimeBetweenTextureUpdates)
        {
            Timer = 0.0f;

            // Ping Pong The Value
            if(AnimationOffset + AnimationOffsetModifier >= SpriteRegion.size() || AnimationOffset + AnimationOffsetModifier < 0)
                AnimationOffsetModifier *= -1;

            AnimationOffset += AnimationOffsetModifier;
        }

        // Finally Set It Into Our Already Existing Sprite Renderer
        // This if isjus to put this sprite in the right pose
        SpriteRenderer.SpriteToRender.setRegion(SpriteRegion.get(OverrideAnimationSection ? 1 : AnimationOffset));

        PreviousDirection.set(CurrentDirection);
    }

    @Override
    public boolean CanUpdate()
    {
        return true;
    }
}
