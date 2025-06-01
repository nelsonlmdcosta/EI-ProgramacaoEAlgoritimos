package Events;

import sun.jvm.hotspot.utilities.Observer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventDispatcherV2<TInterface>
{
    // This Can Only Be Accessed Inside Of This Class, So We Can Leave It's Members To Be Public For Ease Of Use
    private class ObserverData
    {
        public Method MethodToInvoke;
        public TInterface ObjectInterface;

        // Instead Of A Constructor I Need To Know If It managed To Set Everything Up So We Do It This Way
        public boolean InitializeObserverData(Class<?> InterfaceType, TInterface ObserverToInitialize)
        {
            this.ObjectInterface = ObserverToInitialize;
            MethodToInvoke = FindMethod( InterfaceType);

            return MethodToInvoke != null;
        }

        private void InvokeMethod(Object[] params)
        {
            try
            {
                MethodToInvoke.invoke(ObjectInterface, params);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e); // This should never happen afaik, we're careful with the templating and what's available on purspose including during construction
                //System.out.println();
            }
        }

        private Method FindMethod(Class<?> ClassType)
        {
            // Go Through Class Hierarchy To Find The First Implementation Of The Interface,
            // This Allows Base Classes To Implement The Function And Be Registered/Invoked Automatically
            // It onlypicks the first instance, which should be fine for most cases, we can maybe force a
            // Method object to be passed in later
            do
            {
                // Get All Interfaces and check to see if it matches the right one
                Class<?>[] Interfaces = ClassType.getInterfaces();
                for (Class<?> Interface : Interfaces)
                {
                    if(Interface.isAssignableFrom(InterfaceClass))
                    {
                        return Interface.getDeclaredMethods()[0];
                    }
                }

                // If nothing was found here, let's check the parents until there is nothing to check.
                ClassType = ClassType.getSuperclass();

            } while ( ClassType != null );

            return null;
        }
    }

    private final List<ObserverData> ObserverData = new ArrayList<ObserverData>();

    private Class<TInterface> InterfaceClass;

    public EventDispatcherV2(Class<TInterface> InterfaceClass)
    {
        // We have to do this due to type erasure :(
        this.InterfaceClass = InterfaceClass;
    }

    // This enforces that the Event is created and specific Interfaces Can Register
    public void AddObserver(TInterface Observer)
    {
        ObserverData.add(GenerateObserverData( Observer ));
    }

    public void RemoveObserver(TInterface observer)
    {
        for(int i = 0; i < ObserverData.size(); ++i)
        {
            if(ObserverData.get(i).ObjectInterface == observer)
                ObserverData.remove(i);
        }
    }

    public void Invoke(Object... params)
    {
        for (int i = 0; i < ObserverData.size(); ++i)
        {
            ObserverData CurrentObserverData = ObserverData.get(i);
            if(CurrentObserverData != null)
            {
                CurrentObserverData.InvokeMethod(params);
            }
        }
    }

    private ObserverData GenerateObserverData(TInterface ObserverToRegister)
    {
        ObserverData NewObserverData = new ObserverData();
        return NewObserverData.InitializeObserverData( InterfaceClass, ObserverToRegister ) ? NewObserverData : null;
    }
}
