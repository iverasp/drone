package pilot.peripheral.sensor;

import net.sf.marineapi.provider.PositionProvider;
import net.sf.marineapi.provider.event.PositionEvent;
import net.sf.marineapi.provider.event.PositionListener;

public class GPS {

    public GPS() {
        PositionProvider provider = new PositionProvider(null);
        provider.addListener(new PositionListener() {
            @Override
            public void providerUpdate(PositionEvent positionEvent) {
                System.out.println(positionEvent);
                System.out.println("HI");
            }
        });
    }
}
