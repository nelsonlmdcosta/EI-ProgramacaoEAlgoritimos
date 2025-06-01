package EntityComponent.Transform;

import EntityComponent.AEntityComponent;
import EntityComponent.Transform.Events.OnTransformDirtyFlagClearedEvent;
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
    private final Vector2 origin;
    private final Matrix3 transformMatrix;
    private boolean transformDirty;
    public boolean GetIsTransformDirty() { return transformDirty; }

    // Direction vectors (automatically calculated)
    private final Vector2 right;
    private final Vector2 up;

    public OnTransformDirtyFlagClearedEvent OnDirtyFlagCleared = new OnTransformDirtyFlagClearedEvent();

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

        this.origin = new Vector2(0, 0);
        this.transformMatrix = new Matrix3();
        this.transformDirty = true;

        this.right = new Vector2(1, 0);
        this.up = new Vector2(0, 1);

        updateDirectionVectors();
    }

    // Copy constructor
    public Transform(Transform other)
    {
        this.position = new Vector2(other.position);
        this.rotation = other.rotation;
        this.scale = new Vector2(other.scale);

        this.origin = new Vector2(other.origin);
        this.transformMatrix = new Matrix3();
        this.transformDirty = true;

        this.right = new Vector2(other.right);
        this.up = new Vector2(other.up);
    }

    //endregion

    //region Position methods

    public Transform SetPosition(float x, float y)
    {
        position.set(x, y);
        transformDirty = true;
        return this;
    }

    public Transform SetPosition(Vector2 position)
    {
        this.position.set(position);
        transformDirty = true;
        return this;
    }

    public Transform AddPosition(float x, float y)
    {
        position.add(x, y);
        transformDirty = true;
        return this;
    }

    public Transform AddPosition(Vector2 translation)
    {
        position.add(translation);
        transformDirty = true;
        return this;
    }

    public Vector2 GetPosition()
    {
        return position;
    }

    public float getX() {
        return position.x;
    }

    /**
     * Gets the y-coordinate of the position
     * @return The y-coordinate
     */
    public float getY() {
        return position.y;
    }

    /**
     * Sets the x-coordinate of the position
     * @param x The new x-coordinate
     * @return This transform for chaining
     */
    public Transform setX(float x) {
        position.x = x;
        transformDirty = true;
        return this;
    }

    /**
     * Sets the y-coordinate of the position
     * @param y The new y-coordinate
     * @return This transform for chaining
     */
    public Transform setY(float y) {
        position.y = y;
        transformDirty = true;
        return this;
    }

    // Rotation methods

    /**
     * Sets the rotation in degrees
     * @param degrees The rotation in degrees
     * @return This transform for chaining
     */
    public Transform setRotation(float degrees) {
        this.rotation = degrees;
        transformDirty = true;
        updateDirectionVectors();
        return this;
    }

    /**
     * Rotates this transform by the specified amount
     * @param degrees The rotation to add in degrees
     * @return This transform for chaining
     */
    public Transform rotate(float degrees) {
        this.rotation += degrees;
        transformDirty = true;
        updateDirectionVectors();
        return this;
    }

    /**
     * Gets the current rotation in degrees
     * @return The rotation in degrees
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * Gets the current rotation in radians
     * @return The rotation in radians
     */
    public float getRotationRad() {
        return rotation * MathUtils.degreesToRadians;
    }

    /**
     * Sets the rotation in radians
     * @param radians The rotation in radians
     * @return This transform for chaining
     */
    public Transform setRotationRad(float radians) {
        return setRotation(radians * MathUtils.radiansToDegrees);
    }

    // Scale methods

    /**
     * Sets the scale of this transform
     * @param scaleX The x scale factor
     * @param scaleY The y scale factor
     * @return This transform for chaining
     */
    public Transform setScale(float scaleX, float scaleY) {
        scale.set(scaleX, scaleY);
        transformDirty = true;
        return this;
    }

    /**
     * Sets the scale of this transform uniformly
     * @param scale The scale factor for both x and y
     * @return This transform for chaining
     */
    public Transform setScale(float scale) {
        return setScale(scale, scale);
    }

    /**
     * Sets the scale of this transform
     * @param scale The scale vector
     * @return This transform for chaining
     */
    public Transform setScale(Vector2 scale) {
        this.scale.set(scale);
        transformDirty = true;
        return this;
    }

    /**
     * Scales this transform by the specified factors
     * @param scaleX The x scale factor to multiply by
     * @param scaleY The y scale factor to multiply by
     * @return This transform for chaining
     */
    public Transform scale(float scaleX, float scaleY) {
        scale.scl(scaleX, scaleY);
        transformDirty = true;
        return this;
    }

    /**
     * Scales this transform uniformly
     * @param factor The scale factor to multiply by
     * @return This transform for chaining
     */
    public Transform scale(float factor) {
        return scale(factor, factor);
    }

    /**
     * Gets the current scale
     * @return The scale vector (avoid modifying directly)
     */
    public Vector2 getScale() {
        return scale;
    }

    /**
     * Gets the x scale factor
     * @return The x scale factor
     */
    public float getScaleX() {
        return scale.x;
    }

    /**
     * Gets the y scale factor
     * @return The y scale factor
     */
    public float getScaleY() {
        return scale.y;
    }

    // Origin methods

    /**
     * Sets the origin for rotation and scaling
     * @param originX The x-coordinate of the origin
     * @param originY The y-coordinate of the origin
     * @return This transform for chaining
     */
    public Transform setOrigin(float originX, float originY) {
        origin.set(originX, originY);
        transformDirty = true;
        return this;
    }

    /**
     * Sets the origin for rotation and scaling
     * @param origin The origin vector
     * @return This transform for chaining
     */
    public Transform setOrigin(Vector2 origin) {
        this.origin.set(origin);
        transformDirty = true;
        return this;
    }

    /**
     * Gets the current origin
     * @return The origin vector (avoid modifying directly)
     */
    public Vector2 getOrigin() {
        return origin;
    }

    // Direction methods

    /**
     * Gets the current right direction vector (normalized)
     * @return The right vector (1,0) rotated by transform's rotation
     */
    public Vector2 getRight() {
        return right;
    }

    /**
     * Gets the current up direction vector (normalized)
     * @return The up vector (0,1) rotated by transform's rotation
     */
    public Vector2 getUp() {
        return up;
    }

    /**
     * Updates the direction vectors based on current rotation
     */
    private void updateDirectionVectors() {
        float rad = getRotationRad();
        right.set(MathUtils.cos(rad), MathUtils.sin(rad));
        up.set(-MathUtils.sin(rad), MathUtils.cos(rad));
    }

    // Transformation matrix methods

    /**
     * Gets the transformation matrix based on this transform's properties
     * @return The 3x3 transformation matrix for 2D
     */
    public Matrix3 getTransformMatrix() {
        if (transformDirty) {
            updateTransformMatrix();
        }
        return transformMatrix;
    }

    /**
     * Updates the internal transformation matrix
     */
    public void updateTransformMatrix() {
        // Reset to identity
        transformMatrix.idt();

        // Apply translation to position
        transformMatrix.translate(position.x, position.y);

        // Apply rotation around origin
        if (rotation != 0) {
            // Translate to origin
            if (origin.x != 0 || origin.y != 0) {
                transformMatrix.translate(origin.x, origin.y);
            }

            // Apply rotation
            transformMatrix.rotate(rotation);

            // Translate back from origin
            if (origin.x != 0 || origin.y != 0) {
                transformMatrix.translate(-origin.x, -origin.y);
            }
        }

        // Apply scale around origin
        if (scale.x != 1 || scale.y != 1) {
            // Translate to origin
            if (origin.x != 0 || origin.y != 0) {
                transformMatrix.translate(origin.x, origin.y);
            }

            // Apply scale
            transformMatrix.scale(scale.x, scale.y);

            // Translate back from origin
            if (origin.x != 0 || origin.y != 0) {
                transformMatrix.translate(-origin.x, -origin.y);
            }
        }

        transformDirty = false;

        OnDirtyFlagCleared.InvokeEvent();
    }

    /**
     * Transforms a point from local space to world space
     * @param point The point to transform
     * @return The transformed point
     */
    public Vector2 transformPoint(Vector2 point) {
        return point.cpy().mul(getTransformMatrix());
    }

    /**
     * Transforms a point from local space to world space
     * @param x The x-coordinate of the point
     * @param y The y-coordinate of the point
     * @return The transformed point
     */
    public Vector2 transformPoint(float x, float y) {
        return new Vector2(x, y).mul(getTransformMatrix());
    }

    /**
     * Transforms a point from world space to local space
     * @param point The point to transform
     * @return The transformed point in local space
     */
    public Vector2 inverseTransformPoint(Vector2 point) {
        // Create a new matrix that is the inverse of the transform matrix
        Matrix3 inverse = new Matrix3(getTransformMatrix());
        inverse.inv();
        return point.cpy().mul(inverse);
    }

    /**
     * Transforms a direction vector from local space to world space
     * Does not apply translation, only rotation and scale
     * @param direction The direction vector to transform
     * @return The transformed direction
     */
    public Vector2 transformDirection(Vector2 direction) {
        Vector2 result = direction.cpy();
        float rad = getRotationRad();
        float cos = MathUtils.cos(rad);
        float sin = MathUtils.sin(rad);

        float newX = result.x * cos - result.y * sin;
        float newY = result.x * sin + result.y * cos;

        result.x = newX * scale.x;
        result.y = newY * scale.y;

        return result;
    }

    /**
     * Moves this transform toward a target position
     * @param target The target position
     * @param speed The speed to move at
     * @param deltaTime The time elapsed since the last update
     * @return This transform for chaining
     */
    public Transform moveToward(Vector2 target, float speed, float deltaTime) {
        Vector2 direction = new Vector2(target).sub(position);
        float distance = direction.len();

        if (distance <= speed * deltaTime || distance == 0) {
            position.set(target);
        } else {
            direction.nor().scl(speed * deltaTime);
            position.add(direction);
        }

        transformDirty = true;
        return this;
    }

    /**
     * Rotates this transform to look at a target position
     * @param target The target position to look at
     * @return This transform for chaining
     */
    public Transform lookAt(Vector2 target) {
        Vector2 direction = new Vector2(target).sub(position);
        setRotation(MathUtils.atan2(direction.y, direction.x) * MathUtils.radiansToDegrees);
        return this;
    }

    /**
     * Smoothly rotates this transform to look at a target position
     * @param target The target position to look at
     * @param rotationSpeed The speed of rotation in degrees per second
     * @param deltaTime The time elapsed since the last update
     * @return This transform for chaining
     */
    public Transform lookAtSmooth(Vector2 target, float rotationSpeed, float deltaTime) {
        Vector2 direction = new Vector2(target).sub(position);
        float targetAngle = MathUtils.atan2(direction.y, direction.x) * MathUtils.radiansToDegrees;

        float currentAngle = getRotation();
        float angleDiff = ((targetAngle - currentAngle + 180) % 360) - 180;

        if (angleDiff > 180) angleDiff -= 360;
        if (angleDiff < -180) angleDiff += 360;

        float maxRotation = rotationSpeed * deltaTime;
        float newRotation;

        if (Math.abs(angleDiff) <= maxRotation) {
            newRotation = targetAngle;
        } else {
            newRotation = currentAngle + Math.signum(angleDiff) * maxRotation;
        }

        setRotation(newRotation);
        return this;
    }

    /**
     * Makes a shallow copy of this transform
     * @return A new Transform with the same values
     */
    public Transform copy() {
        return new Transform(this);
    }

    /**
     * Resets this transform to default values
     * @return This transform for chaining
     */
    public Transform reset() {
        position.set(0, 0);
        scale.set(1, 1);
        rotation = 0;
        origin.set(0, 0);
        transformDirty = true;
        updateDirectionVectors();
        return this;
    }

    @Override
    public String toString() {
        return "Transform{" +
            "position=" + position +
            ", scale=" + scale +
            ", rotation=" + rotation +
            ", origin=" + origin +
            '}';
    }
}
