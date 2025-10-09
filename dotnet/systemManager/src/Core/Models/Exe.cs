namespace SystemManager.Core.Models
{
    public class Exe(string name, string uri)
    {
        public string Name { get; set; } = name;
        public Uri Uri { get; set; } = new Uri(uri);

        public static List<Exe> ListFromArgs(string[] args)
        {
            return [.. args
                .Select(arg => arg.Split('|'))
                .Select(parts => new Exe(parts[0], parts[1]))];
        }
    }
}