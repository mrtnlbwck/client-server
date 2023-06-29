using System;
using System.IO;
using System.Net.Sockets;

class Program
{
    static void Main(string[] args)
    {
        TcpClient socket = null;
        string host = "localhost";
        StreamReader streamIn = null;
        StreamWriter streamOut = null;
        string url;

        try
        {
            socket = new TcpClient(host, 8080);
            streamIn = new StreamReader(socket.GetStream());
            streamOut = new StreamWriter(socket.GetStream());

            Console.WriteLine("Server connected.");
            Console.Write("Enter URL: ");

            while (true)
            {
                url = Console.ReadLine();

                // Wysyłanie żądania GET do serwera
                streamOut.WriteLine("GET " + url + " HTTP/1.1");
                streamOut.WriteLine("Host: " + host);
                streamOut.WriteLine();
                streamOut.Flush();

                // Odczytywanie danych zwróconych przez serwer
                string line;
                while ((line = streamIn.ReadLine()) != null)
                {
                    Console.WriteLine(line);
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("Error: " + ex.Message);
        }
        finally
        {
            if (socket != null)
            {
                socket.Close();
            }
            if (streamIn != null)
            {
                streamIn.Close();
            }
            if (streamOut != null)
            {
                streamOut.Close();
            }
        }
    }
}
