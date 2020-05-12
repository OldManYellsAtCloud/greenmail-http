import React, {
	Component
} from 'react'
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  useParams
} from "react-router-dom";

import axios from 'axios'
import Alert from 'react-bootstrap/Alert'
import Table from 'react-bootstrap/Table'
import Container from 'react-bootstrap/Container'

import {ListMailboxes} from '../c/HgmUrl'
import ListFolderRow from './ListFolderRow'

class ListFolderPage extends Component {

	constructor(props) {
		super(props)
		this.state = {
			data: [],
			error: false
		}
		this.reload = this.reload.bind(this)
	}

	reload () {
		console.log("***** componentDidMount")
		let {email} = this.props.match.params

		let url = ListMailboxes(email)
		axios.get(url)
			.then(res => {
				console.log(res)
				for(let i = 0 ; i < res.data.length ; ++i) {
					res.data[i].id = '' + i
				}
				this.setState({
					data: res.data
				})
			}, (error) => {
				this.setState({
					data: error,
					url: url,
					error: true
				})
			})
	}

	componentDidMount() {
		this.reload()
	}

	render() {

		if (this.state.error) {
			let eMessage = this.state.data.toString() + " " +this.state.url
			return <Alert variant="danger" dismissible>{eMessage}</Alert>
		}

		let email = ''
		//let o = useParams()
		console.log("*****")
		console.log(this.props)
		return (
		<Container>
		<h2>Mailboxes: {email}</h2>

		<Table className="table">
			<tbody>
			<tr>
				<th>Actions</th>
				<th>FQN</th>
				<th>Name</th>
				<th># Messages</th>
			</tr>
			{
				this.state.data.map(folder => <ListFolderRow key={folder.id} email={email} folder={folder} reload={this.reload}/>)
			}
			</tbody>
		</Table>
		</Container>
		)
	}
}

export default ListFolderPage
