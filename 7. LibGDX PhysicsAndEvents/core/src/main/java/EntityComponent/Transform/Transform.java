package EntityComponent.Transform;

import EntityComponent.AEntityComponent;
import EntityComponent.Transform.Events.IOnTransformDirtyFlagCleared;
import Events.EventDispatcherV2;
import WorldScene.WorldScene;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

/**
 * Transform component for 2D entities in LibGDX.
 * Handles position, rotation, scale, and related transformations.
 * Most of this is based of some script online, I've left this for too long and forgotten it but I'll try find it later.
 * Other components that rely on this positioning will be warned of changes when a dirty cleanup is done
 */
public class Transform extends AEntityComponent
{
    // Core transform properties
    private Vector2 position;
    private Vector2 scale;
    private float rotation; // in degrees

    // Cached vectors and matrices to avoid frequent memory allocations
    private boolean transformDirty;
    public boolean GetIsTransformDirty() { return transformDirty; }
    private void SetDirtyTransform()
    {
        transformDirty = true;
        WorldScene.NotifyDirtyTransform(this);
    }

    // Direction vectors (automatically calculated)
    private final Vector2 right;
    private final Vector2 up;

    public EventDispatcherV2<IOnTransformDirtyFlagCleared> OnTransformDirtyFlagClearedEvent = new EventDispatcherV2<IOnTransformDirtyFlagCleared>(IOnTransformDirtyFlagCleared.class);

    //region Constructors

    // Creates a transform at position (0,0) with no rotation and scale (1,1)
    public Transform()
    {
        this(0, 0);
    }

    // Creates a transform at the specified position with no rotation and scale (1,1)
    public Transform(float x, float y)
    {
        this(x, y, 0);
    }

    // Creates a transform with specified position and rotation, and scale (1,1)
    public Transform(float x, float y, float rotation)
    {
        this(x, y, rotation, 1, 1);
    }

    // Creates a transform with specified position, rotation and scale
    public Transform(float x, float y, float rotation, float scaleX, float scaleY)
    {
        this.position = new Vector2(x, y);
        this.rotation = rotation;
        this.scale = new Vector2(scaleX, scaleY);

        this.right = new Vector2(1, 0);
        this.up = new Vector2(0, 1);

        UpdateDirectionVectors();
    }

    // Copy constructor
    public Transform(Transform other)
    {
        this.position = new Vector2(other.position);
        this.rotation = other.rotation;
        this.scale = new Vector2(other.scale);

        this.right = new Vector2(other.right);
        this.up = new Vector2(other.up);
    }

    public void Start()
    {
        SetDirtyTransform();
    }

    //endregion

    //region Position methods

    public Transform SetPosition(float x, float y)
    {
        position.set(x, y);
        SetDirtyTransform();
        return this;
    }

    public Transform SetPosition(Vector2 position)
    {
        this.position.set(position);
        SetDirtyTransform();
        return this;
    }

    public Transform AddPosition(float x, float y)
    {
        position.add(x, y);
        SetDirtyTransform();
        return this;
    }

    public Transform AddPosition(Vector2 translation)
    {
        position.add(translation);
        SetDirtyTransform();
        return this;
    }

    public Vector2 GetPosition()
    {
        return position;
    }

    // Rotation methods

    public Transform SetRotation(float degrees)
    {
        this.rotation = degrees;
        UpdateDirectionVectors();
        SetDirtyTransform();
        return this;
    }

    public Transform AddRotatation(float degrees)
    {
        this.rotation += degrees;
        UpdateDirectionVectors();
        SetDirtyTransform();
        return this;
    }

    public float GetRotation()
    {
        return rotation;
    }

    public float GetRotationRadians()
    {
        return rotation * MathUtils.degreesToRadians;
    }


    public Transform SetRotationRad(float radians)
    {
        return SetRotation(radians * MathUtils.radiansToDegrees);
    }

    // Scale methods


    public Transform SetScale(float scaleX, float scaleY)
    {
        scale.set(scaleX, scaleY);
        transformDirty = true;
        return this;
    }

    public Transform SetScale(float scale)
    {
        return SetScale(scale, scale);
    }

    public Transform SetScale(Vector2 scale)
    {
        this.scale.set(scale);
        transformDirty = true;
        return this;
    }

    public Vector2 GetScale()
    {
        return scale;
    }

    // Direction methods

    public Vector2 GetUp()
    {
        return up;
    }

    public Vector2 GetRight()
    {
        return right;
    }

    /**
     * Updates the direction vectors based on current rotation
     */
    private void UpdateDirectionVectors()
    {
        float rad = GetRotationRadians();
        right.set(MathUtils.cos(rad), MathUtils.sin(rad));
        up.set(-MathUtils.sin(rad), MathUtils.cos(rad));
    }

    // Transformation matrix methods

    // TODO: If We Bring Back Matrices Then We Update Them here
    public void ClearDirtyTransform()
    {
        transformDirty = false;

        OnTransformDirtyFlagClearedEvent.Invoke();
    }

    /**
     * Transforms a direction vector from local space to world space
     * Does not apply translation, only rotation and scale
     * @param direction The direction vector to transform
     * @return The transformed direction
     */
    public Vector2 TransformDirection(Vector2 direction)
    {
        Vector2 result = direction.cpy();
        float rad = GetRotationRadians();
        float cos = MathUtils.cos(rad);
        float sin = MathUtils.sin(rad);

        float newX = result.x * cos - result.y * sin;
        float newY = result.x * sin + result.y * cos;

        result.x = newX * scale.x;
        result.y = newY * scale.y;

        return result;
    }

    public Transform LookAt(Vector2 target)
    {
        Vector2 direction = new Vector2(target).sub(position);
        SetRotation(MathUtils.atan2(direction.y, direction.x) * MathUtils.radiansToDegrees);
        return this;
    }

    @Override
    public String toString()
    {
        return "Transform{ position=" + position + ", scale=" + scale + ", rotation=" + rotation + "}";
    }
}
