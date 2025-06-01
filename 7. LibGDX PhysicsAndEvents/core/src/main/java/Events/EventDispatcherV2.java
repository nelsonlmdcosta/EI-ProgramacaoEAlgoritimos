package Events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventDispatcherV2<TInterface>
{
    // This Can Only Be Accessed Inside Of This Class, So We Can Leave It's Members To Be Public For Ease Of Use
    // It's basically a clean way to encapsulate all the data we need for any observer and invoke it.
    private class ObserverData
    {
        public Method MethodToInvoke;
        public TInterface ObjectInterface;

        // Instead Of A Constructor I Need To Know If It managed To Set Everything Up So We Do It This Way
        public boolean InitializeObserverData(Class<?> InterfaceType, TInterface ObserverToInitialize)
        {
            this.ObjectInterface = ObserverToInitialize;
            MethodToInvoke = FindMethod( ObserverToInitialize.getClass() );

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

    // This needs to be done to avoid Type Erasure in Java :l
    private final Class<TInterface> InterfaceClass;
    private final List<ObserverData> ObserverData = new ArrayList<ObserverData>();

    public EventDispatcherV2(Class<TInterface> InterfaceClass)
    {
        // We have to do this due to type erasure :(
        this.InterfaceClass = InterfaceClass;
    }

    // This enforces that the Event is created and specific Interfaces Can Register
    public void AddObserver(TInterface Observer)
    {
        ObserverData NewObserver = GenerateObserverData( Observer );
        if(NewObserver != null)
            ObserverData.add(NewObserver);
    }

    public void RemoveObserver(TInterface observer)
    {
        // Normally when removing more than one element you reverse the for loop, this will avoid missed indexes,
        // In this case we remove only one element so we move on once the job is done
        for(int i = 0; i < ObserverData.size(); ++i)
        {
            if (ObserverData.get(i).ObjectInterface == observer)
            {
                ObserverData.remove(i);
                return;
            }
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
