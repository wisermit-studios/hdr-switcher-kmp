using System.Diagnostics;
using SystemManager.Model;
using SystemManager.Util;

namespace SystemManager.Core;

public static class Launcher
{
    public static void Launch(Application app)
    {
        using var process = new Process();
        process.StartInfo.FileName = app.File.Name;

        // TODO: HDR from app config.

        if (HdrManager.IsEnabled())
        {
            Log.D("HDR already enabled.");

            process.Start();
        }
        else
        {
            HdrManager.SetHdrEnabled(true);

            process.Start();

            Log.D($"Waiting for {app.File.Name}...");

            process.WaitForExit();

            HdrManager.SetHdrEnabled(false);
        }
    }
}