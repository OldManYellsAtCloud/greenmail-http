import React, {Component} from 'react'
import axios from 'axios'
import Alert from 'react-bootstrap/Alert'

import {ListUserMessageUrl} from './GmhUrl'
import MessagesTable from './MessagesTable'

class EmailDestinationList extends Component {

	constructor(props) {
		super(props)
		this.state = {
			data: [],
			error: false
		}
		this.reload = this.reload.bind(this)
	}

	componentDidMount() {
		this.reload()
	}

	componentDidUpdate(prevProps, prevState) {
		if (prevProps.email !== this.props.email) {
			this.reload()
		}
	}

	reload() {
		this.url = ListUserMessageUrl(this.props.email, this.props.who)
		axios.get(this.url)
			.then(res => {
				for (let i = 0 ; i < res.data.length ; ++i) {
					res.data[i].id = '' + i
				}
				this.setState({
					data: res.data
				})
			}, (error) => {
				this.setState({
					data: error,
					error: true
				})
			})

	}

	render() {
		if (this.state.error) {
			let eMessage = this.state.data.toString() + " " +this.url
			return <Alert variant="danger" dismissible>{eMessage}</Alert>
		}

		return (
			<div>
			<b>{this.props.who}:</b>
			<MessagesTable messages={this.state.data} reload={this.reload} who={this.props.who}/>
			</div>
		)
	}
}

export default EmailDestinationList
