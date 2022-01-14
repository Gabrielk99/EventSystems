import sys
from pathlib import Path
def generateJson(name,cordinates,id,vacina,email="",celular="",T_default=0,T_med=0,T_max=0,time_max=0,time_increment=0):
    if not vacina:
        Path(f'Gestores/Gestor{id}').mkdir(parents=True,exist_ok=True);

        file = open(f'Gestores/Gestor{id}/rota.json','w');

        file.write(f'{"{"}\n\t"name":"{name}",\n\t"id":{id},\n\t"coordinates":{cordinates}\n"email":{email},\n"celular":{celular}\n{"}"}\n')

    else:
        Path(f'Vacinas/Vacina_{name}').mkdir(parents=True,exist_ok=True);

        file = open(f'Vacinas/Vacina_{name}/configure.json','w')

        if(time_increment!=0):
            file.write(f'{"{"}\n\t"name":"{name}",\n\t"id":{id},\n\t"t_init":{T_default},\n\t"t_med":{T_med},\n\t"t_max":{T_max},\n\t"coordinates":{cordinates},\n\t"time_max":{time_max},\n\t"time_increment":{time_increment}\n{"}"}\n')
        else:
            file.write(f'{"{"}\n\t"name":"{name}",\n\t"id":{id},\n\t"t_init":{T_default},\n\t"t_med":{T_med},\n\t"t_max":{T_max},\n\t"coordinates":{cordinates},\n\t"time_max":{time_max}\n{"}"}\n')



def readCordinates(cordinates):

    list_of_cordinates = cordinates.readlines()
    
    save_cordinates = []
    for ponto in list_of_cordinates:
        lat_long = ponto.split(',')[:2]
        lat = float(lat_long[0])
        long = float(lat_long[1])
        save_cordinates.append([lat,long])
    
    
    
    return save_cordinates
def main (args):
    vacine = args[4]=="True";
    if(len(args)<7):
        print("""
            \n\nplease execute the program with 6 parameters,
            \n-- first is name of object, the second is the id,\n,
            -- third is the local_path to cordinates.txt\n
            -- fourth is the option if the object is vacine or gestor, False or True 
            -- fifth is the email for the manager\n
            -- sixth is the cellnumber for the manager (+55 DDD 9XXXX-XXXX) \n\n """)
        sys.exit()
    elif(vacine and len(args)<9):
        print("your object is vacine, please insert the temperatures like")
        print("-- fifth argument is default temperature")
        print("-- sixth argument is median temperature")
        print("-- seventh argument is max temperature")
        print("-- eighth argument is max time after max temperature")
        print("-- nineth argument is time of wait increment is optional")
        sys.exit()
    cordinates = open(args[3])
    list_of_cordinates = readCordinates(cordinates);
    if(vacine):
        if(len(args)==10):
            generateJson(args[1],list_of_cordinates,args[2],vacine,float(args[5]),float(args[6]),float(args[7]),float(args[8]),float(args[9]));
        else:
            generateJson(args[1],list_of_cordinates,args[2],vacine,float(args[5]),float(args[6]),float(args[7]),float(args[8]));
    else:
        generateJson(args[1],list_of_cordinates,args[2],vacine)


if __name__=="__main__":
    main(sys.argv);