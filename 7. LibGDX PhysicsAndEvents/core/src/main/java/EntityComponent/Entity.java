package EntityComponent;

import EntityComponent.Transform.Transform;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import WorldScene.WorldScene;

import java.util.ArrayList;
import java.util.List;

public class Entity
{
    public Entity(WorldScene WorldSceneContext, String EntityName, List<Integer> EntityTags)
    {
        this.EntityName = EntityName;
        this.EntityTags = EntityTags;
        this.WorldScene = WorldSceneContext;

        // All Entities will be represented as a transform
        AddComponent(new Transform());
    }

    public String EntityName;
    public List<Integer> EntityTags;

    public WorldScene WorldScene = null;

    private ArrayList<AEntityComponent> EntityComponents = new ArrayList<>();

    // Cache These For Faster Processing
    private ArrayList<IUpdateableComponent> UpdateableComponents = new ArrayList<>();
    private ArrayList<IRenderableComponent> RenderableComponents = new ArrayList<>();

    // Generic Types, We Can Expose These Later
    // This way we can search for any type of component
    public <T extends AEntityComponent> T GetFirstComponentOfType(Class<T> ComponentClassType)
    {
        for(int i = 0; i < EntityComponents.size(); ++i)
        {
            AEntityComponent Component = EntityComponents.get(i);
            if(Component != null && ComponentClassType.isAssignableFrom(Component.getClass()) )
            {
                return ComponentClassType.cast(Component);
            }
        }

        return null;
    }

    public <T> T GetFirstComponentWithInterface(Class<T> componentClassType)
    {
        for (int i = 0; i < EntityComponents.size(); ++i)
        {
            AEntityComponent component = EntityComponents.get(i);
            if (component != null && componentClassType.isAssignableFrom(component.getClass()))
            {
                return componentClassType.cast(component);
            }
        }
        return null;
    }

    // Add Component
    public <T extends AEntityComponent> T AddComponent(AEntityComponent ComponentToAdd)
    {
        EntityComponents.add(ComponentToAdd);
        ComponentToAdd.Entity(this);
        ComponentToAdd.World(WorldScene);

        // Cache All Updateable and Renderable Components
        if(ComponentToAdd instanceof IUpdateableComponent)
            UpdateableComponents.add((IUpdateableComponent)ComponentToAdd);

        if(ComponentToAdd instanceof IRenderableComponent)
            RenderableComponents.add((IRenderableComponent)ComponentToAdd);

        return (T)ComponentToAdd;
    }

    // Remove Component
    public <T extends AEntityComponent> void RemoveFirstComponentOfType(Class<T> ComponentClassType)
    {
        for(int i = 0; i < EntityComponents.size(); ++i)
        {
            AEntityComponent Component = EntityComponents.get(i);
            if(Component != null && ComponentClassType.isAssignableFrom(Component.getClass()) )
            {
                EntityComponents.remove(i);

                if(Component instanceof IUpdateableComponent)
                    UpdateableComponents.remove((IUpdateableComponent)Component);

                if(Component instanceof IRenderableComponent)
                    RenderableComponents.remove((IRenderableComponent)Component);

                return;
            }
        }
    }

    public void Start()
    {
        for(int i = 0; i < EntityComponents.size(); ++i)
        {
            EntityComponents.get(i).Start();
        }
    }

    public void Update(float DeltaTime)
    {
        for(int i = 0; i < UpdateableComponents.size(); ++i)
        {
            IUpdateableComponent CurrentComponent = UpdateableComponents.get(i);
            if (CurrentComponent != null && CurrentComponent.CanUpdate())
                CurrentComponent.Update(DeltaTime);
        }
    }

    public void Render(SpriteBatch Batch)
    {
        for(int i = 0; i < RenderableComponents.size(); ++i)
        {
            IRenderableComponent CurrentComponent = RenderableComponents.get(i);
            if (CurrentComponent != null && CurrentComponent.CanRender())
            {
                CurrentComponent.Render(Batch);
            }
        }
    }
}
