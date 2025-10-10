using SystemManager.Framework;
using Vortice.DXGI;
using WindowsInput;
using WindowsInput.Native;

namespace SystemManager.Core
{
    public static class HdrManager
    {
        public static bool IsEnabled()
        {
            uint adapterIndex = 0;

            using var factory = DXGI.CreateDXGIFactory1<IDXGIFactory6>();
            while (factory.EnumAdapters1(adapterIndex, out IDXGIAdapter1 adapter).Success)
            {
                using (adapter)
                {
                    uint outputIndex = 0;
                    while (adapter.EnumOutputs(outputIndex, out IDXGIOutput output).Success)
                    {
                        using (output)
                        {
                            var output6 = output.QueryInterfaceOrNull<IDXGIOutput6>();
                            if (output6?.Description1.ColorSpace == ColorSpaceType.RgbFullG2084NoneP2020)
                            {
                                return true;
                            }
                        }

                        outputIndex++;
                    }
                }

                adapterIndex++;
            }

            return false;
        }


        public static void Toggle()
        {
            Log.D("Toggling HDR.");

            new InputSimulator().Keyboard.ModifiedKeyStroke(
                [VirtualKeyCode.LWIN, VirtualKeyCode.MENU], // Win + Alt
                VirtualKeyCode.VK_B
            );
        }
    }
}