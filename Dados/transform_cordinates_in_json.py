import sys
from pathlib import Path
def generateJson(name,cordinates,id,vacina,T_default=0,T_med=0,T_max=0):
    if not vacina:
        Path(f'Gestores/Gestor{id}').mkdir(parents=True,exist_ok=True);

        file = open(f'Gestores/Gestor{id}/rota.json','w');

        file.write(f'{"{"}\n\t"name":"{name}",\n\t"id":{id},\n\t"cordinates":{cordinates}\n{"}"}\n')

    else:
        Path(f'Vacinas/Vacina_{name}').mkdir(parents=True,exist_ok=True);

        file = open(f'Vacinas/Vacina_{name}/configure.json','w')

        file.write(f'{"{"}\n\t"name":"{name}",\n\t"id":{id},\n\t"t_init":{T_default},\n\t"t_med":{T_med},\n\t"t_max":{T_max},\n\t"cordinates":{cordinates}\n{"}"}\n')




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
    if(len(args)<5):
        print("\n\nplease execute the program with 3 parameters,\n the first is name of object, the second is the id,\n, third is the local_path to cordinates.txt\n and four is the option if the object is vacine or gestor, False or True\n\n")
        sys.exit()
    elif(vacine and len(args)<8):
        print("your object is vacine, please insert the temperatures like")
        print("the fifth argument is default temperature")
        print("the sixth argument is median temperature")
        print("the seventh argument is max temperature")
        sys.exit()
    cordinates = open(args[3])
    list_of_cordinates = readCordinates(cordinates);
    if(vacine):
        generateJson(args[1],list_of_cordinates,args[2],vacine,float(args[5]),float(args[6]),float(args[7]));



if __name__=="__main__":
    main(sys.argv);