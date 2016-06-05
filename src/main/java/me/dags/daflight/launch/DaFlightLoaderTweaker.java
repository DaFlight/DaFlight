package me.dags.daflight.launch;

import java.io.File;
import java.util.List;

/**
 * @author dags_ <dags@dags.me>
 */

public class DaFlightLoaderTweaker extends DaFlightTweaker
{
    @Override
    public void acceptOptions(List<String> list, File gameDir, File assetsDir, String profile)
    {}

    @Override
    public String[] getLaunchArguments()
    {
        return new String[0];
    }
}
