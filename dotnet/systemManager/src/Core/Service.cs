using SystemManager.Util;
using SystemManager.Model;

namespace SystemManager.Core
{
    public class Service
    {
        private bool _initialHdrStatus = false;

        private readonly ProcessWatcher _processWatcher;

        public Service()
        {
            _processWatcher = new ProcessWatcher();
        }

        public void SetExecutables(List<Exe> executables)
        {
            _processWatcher.Watch(
                executables,
                onStart: () =>
                {
                    Log.I($"Process started.");
                    EnsureHdrStatus();
                },
                onFinish: () =>
                {
                    Log.I($"Process ended.");
                    RestoreHdrStatus();
                }
            );
        }

        public void Stop()
        {
            _processWatcher.Dispose();
        }

        private void EnsureHdrStatus()
        {
            _initialHdrStatus = HdrManager.IsEnabled();

            if (!_initialHdrStatus)
            {
                HdrManager.Toggle();
            }
        }

        private void RestoreHdrStatus()
        {
            if (_initialHdrStatus != HdrManager.IsEnabled())
            {
                HdrManager.Toggle();
            }
        }
    }
}