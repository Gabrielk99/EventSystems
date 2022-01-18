import './App.css';
import Presentation from './components/presentation/Presentation';
import TemperatureGraph from './components/graphs/temperature/Temperature';
import Map from './components/graphs/maps/Map';

function App() {
  return (
    <div className="App">
      <Presentation/>
      <div className='data-row'>
        <TemperatureGraph/>
        <Map/>
      </div>
      
    </div>
  );
}

export default App;
