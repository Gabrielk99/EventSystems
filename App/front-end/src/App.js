import './App.css';
import Presentation from './components/presentation/Presentation';
import TemperatureGraph from './components/graphs/temperature/Temperature';
import Map from './components/graphs/maps/Map';
import { useEffect, useState } from 'react';

function App() {
  const [colorsVaccine,setColorsVaccine] = useState({});

  useEffect(()=>{
    // console.log(colorsVaccine)
  },[colorsVaccine])

  const handleColorVaccine = (colors)=>{
  
    setColorsVaccine(colors);
  }
  return (
    <div className="App">
      <Presentation/>
      <div className='data-row'>
        <TemperatureGraph updateColorVaccine={handleColorVaccine}/>
        <Map colorsToVaccine={colorsVaccine}/>
      </div>
      
    </div>
  );
}

export default App;
