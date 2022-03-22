import './App.css';
import Presentation from './components/presentation/Presentation';
import TemperatureGraph from './components/graphs/temperature/Temperature';
import Map from './components/graphs/maps/Map';
import { useEffect, useState } from 'react';
import { getAllVaccineInformation,getAllStatusVaccines} from './controllers/vacinas/vacinaController';
import {getAllManagerLocation,getAllManagerInformation} from './controllers/gestores/gestorController';
import useWebSocket  from 'react-use-websocket';
import{ HtmlBody, getHTMLBODY, getAddressFromLatLong } from './logic/Geocoding';
import { Status } from './models/Vaccine';
import { useGlobalState } from './logic/GlobalHook';
import Notify from './components/notifications/Notify';
import { setEmailSender, setSendEmail } from './controllers/email/emailController';
function App() {

  const [colorsVaccine,setColorsVaccine] = useState({});
  const [vaccineDatasToControl,setVaccineDatasToControl] = useState([]);
  const [vaccines,setVaccines] = useState({});
  const [managerData,setManagerData] = useState([]);
  const [managers,setManagers] = useState([]);
  const [timeOut, setTimeOut] = useState(false);
  const [vaccinesStatusNotify,setVaccinesStatusNotify] = useGlobalState('status')
  const [emailSend,setEmailSend] = useState(false);
  const [ownerKey,setOwnerKey] = useState("mikaella");
  const {
    sendMessage,
    lastMessage,
    readyState
  } = useWebSocket('ws://localhost:3001/api/vacina/status');

  useEffect( ()=>{
    async function fetchData(){
      await getVaccinesInformation();
      await getAllManagerLocationInformation();

      if(lastMessage!==null){
        setVaccineDatasToControl(JSON.parse(lastMessage.data));
      }     
    }
    fetchData();
  },[lastMessage])

  useEffect(()=>{
    if(vaccineDatasToControl!==null){
      const vaccinesToNotify = vaccineDatasToControl.map((vaccineData)=>{
        const vaccine = {id:vaccineData.id,...vaccineData.datasSaved[vaccineData.datasSaved.length-1]};
        // console.log(vaccineData)
        return {
          name:vaccines[vaccine.id],
          location:vaccine.location,
          status:vaccine.status,
          id:vaccine.id
        }
      })
      setVaccinesStatusNotify(vaccinesToNotify)
    }
  },[vaccineDatasToControl])
  
  useEffect(()=>{
    setSendEmail(emailSend)
  },[emailSend])

  useEffect(()=>{
    if(ownerKey==='mikaella'){
      setEmailSender(0)
    }
    else{
      setEmailSender(1)
    }
  },[ownerKey])
  
  const handleColorVaccine = (colors)=>{
    setColorsVaccine(colors);
  }
  
  const getVaccinesInformation = async ()=>{
    const vaccinesInfo = await getAllVaccineInformation();

    if(!vaccinesInfo.status){
      vaccinesInfo.map(vaccine=>{
          const vaccineInfo = vaccines
          vaccineInfo[vaccine.id] = vaccine.name;
          setVaccines({...vaccineInfo});
      })
    }
    
  }

  const getAllManagerLocationInformation = async ()=>{
    const managersInfo = await getAllManagerInformation();

    if(!managersInfo.status){
      managersInfo.map(manager=>{
        const managerInfo = managers;
        managerInfo[manager.id]={ 
          name:manager.name,
          email:manager.email,
          cel:manager.celular,
          img:manager.img
        };
        setManagers({...managerInfo});
      })
     }
    


    await  getAllManagerLocation().then(data=>setManagerData(data));
  }
  const handleSendemail = async ()=>{
      setEmailSend(!emailSend);
  }
  const handleChangeAPIKEY = async ()=>{
    if(ownerKey === 'mikaella'){
      setOwnerKey('gabriel');
    }
    else{
      setOwnerKey('mikaella');
    }
  }
  return (
    <div className="App">
      <Presentation/>
      <div style={{display:'flex',flexDirection:'row',width:'100%',justifyContent:'space-around',marginTop:'5%',marginBottom:'5%'}}>
        <div className={ownerKey==='mikaella'?'div-button-changeKey':'div-button-changeKey gabriel'}>
          <div onClick={handleChangeAPIKEY} className='button-changerKey'>
            Troca KEY
          </div>
            Owner Key: {ownerKey}
        </div> 

        <div className={emailSend?'div-button-sendemail active':'div-button-sendemail'}>
          <div onClick={handleSendemail} className='button-sendemail'>
              SendEmail
          </div>
            Status: {String(emailSend)}
        </div> 
      </div>
      <Notify/>
      <div className='data-row'>
        <TemperatureGraph updateColorVaccine={handleColorVaccine} datas={vaccineDatasToControl} vaccines={vaccines}/>
        <Map colorsToVaccine={colorsVaccine} datas={vaccineDatasToControl} vaccines={vaccines} managers={managers} datasToManagers={managerData}/>
      </div>
      
    </div>
  );
}

export default App;
